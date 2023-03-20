package pt.isel.ls.server.data.userData

import pt.isel.ls.User
import pt.isel.ls.server.data.getNextId
import pt.isel.ls.server.data.users
import java.util.*

class DataUser : IDataUser {
    override fun createUser(name: String, email: String): Pair<Int, String> {
        val token = UUID.randomUUID().toString()
        val newUser = User(getNextId(User::class.java), email, name, token)
        users.add(newUser)
        return Pair(newUser.idUser, token)
    }

    override fun getUser(idUser: Int): User? {
        return users.find { it.idUser == idUser }
    }

    override fun getUser(token: String?): User? {
        return users.find { it.token == token }
    }

    override fun checkEmail(email: String): Boolean {
        return users.none { it.email == email }
    }

}