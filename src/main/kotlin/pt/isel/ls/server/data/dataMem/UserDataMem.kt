package pt.isel.ls.server.data.dataMem

import pt.isel.ls.server.utils.User
import pt.isel.ls.server.data.getNextId
import pt.isel.ls.server.data.dataInterfaces.UserData
import pt.isel.ls.server.data.users
import pt.isel.ls.server.exceptions.TrelloException
import java.util.*

class UserDataMem : UserData {
    override fun createUser(name: String, email: String): Pair<Int, String> {
        val token = UUID.randomUUID().toString()
        val newUser = User(getNextId(User::class.java), email, name, token)
        users.add(newUser)
        return Pair(newUser.idUser, token)
    }

    override fun getUser(token: String): User {
        return users.find { it.token == token } ?: throw TrelloException.NotAuthorized()
    }

    override fun checkEmail(email: String) {
        if(users.any { it.email == email }) throw TrelloException.AlreadyExists(email)
    }
}