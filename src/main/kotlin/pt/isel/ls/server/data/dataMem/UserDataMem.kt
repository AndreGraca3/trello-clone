package pt.isel.ls.server.data.dataMem

import pt.isel.ls.server.data.dataInterfaces.models.UserData
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.User
import java.sql.Connection
import java.util.*

class UserDataMem : UserData {

    val users = mutableListOf<User>(User(1, "alberto.tremocos@gmail.com", "Jose", "token123", "https://live.staticflickr.com/65535/52841364369_13521f6ef1_m.jpg"))

    override fun createUser(name: String, email: String, con: Connection): Pair<Int, String> {
        val token = UUID.randomUUID().toString()
        val newUser = User(getNextId(), email, name, token, "https://i.imgur.com/JGtwTBw.png")
        users.add(newUser)
        return Pair(newUser.idUser, token)
    }

    override fun getUser(token: String, con: Connection): User {
        return users.find { it.token == token } ?: throw TrelloException.NotAuthorized()
    }

    override fun getUser(idUser: Int, con: Connection): User {
        return users.find { it.idUser == idUser } ?: throw TrelloException.NotFound("User")
    }

    override fun getUsers(idBoard: Int, limit: Int?, skip: Int?, con: Connection): List<User> {
        //return users.filter { idUsers.contains(it.idUser) }.subList(skip, skip + limit)
        TODO("Not yet implemented!")
    }

    override fun changeAvatar(token: String, avatar: String, con: Connection) {
        val user = getUser(token, con)
        users[users.indexOf(user)] = user.copy(avatar = avatar)
    }

    private fun getNextId(): Int {
        return if (users.isEmpty()) 1 else users.last().idUser + 1
    }
}
