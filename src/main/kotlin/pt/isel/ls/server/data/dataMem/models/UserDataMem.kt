package pt.isel.ls.server.data.dataMem.models

import pt.isel.ls.server.data.transactionManager.transactions.TransactionCtx
import pt.isel.ls.server.data.transactionManager.transactions.MemTransaction
import pt.isel.ls.server.data.dataInterfaces.models.UserData
import pt.isel.ls.server.data.dataMem.users
import pt.isel.ls.server.data.dataMem.usersBoards
import pt.isel.ls.server.exceptions.ALREADY_EXISTS
import pt.isel.ls.server.exceptions.INVAL_PARAM
import pt.isel.ls.server.exceptions.NOT_FOUND
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.User
import pt.isel.ls.server.utils.checkPaging
import java.sql.SQLException
import java.util.*

class UserDataMem : UserData {

    override fun createUser(name: String, email: String, ctx: TransactionCtx): Pair<Int, String> {
        if (name.length > 20) throw SQLException("$INVAL_PARAM name is too long.", "22001")
        if (users.any { it.email == email }) throw SQLException("$email $ALREADY_EXISTS", "23505")
        if (users.any { it.name == name }) throw SQLException("$name $ALREADY_EXISTS", "23505")
        val token = UUID.randomUUID().toString()
        val newUser = User(getNextId(), email, name, token, "https://i.imgur.com/JGtwTBw.png")
        users.add(newUser)
        return Pair(newUser.idUser, token)
    }

    override fun getUser(token: String, ctx: TransactionCtx): User {
        return users.find { it.token == token } ?: throw TrelloException.NotAuthorized()
    }

    override fun getUser(idUser: Int, ctx: TransactionCtx): User {
        return users.find { it.idUser == idUser } ?: throw TrelloException.NotFound("User $NOT_FOUND")
    }

    override fun getUsers(idBoard: Int, ctx: TransactionCtx): List<User> {
        return users.filter { user ->
            usersBoards.filter { it.idBoard == idBoard }.map { it.idUser }.contains(user.idUser)
        }
    }

    override fun changeAvatar(token: String, avatar: String, ctx: TransactionCtx) {
        val user = getUser(token, (ctx as MemTransaction))
        users[users.indexOf(user)] = user.copy(avatar = avatar)
    }

    private fun getNextId(): Int {
        return if (users.isEmpty()) 1 else users.last().idUser + 1
    }
}
