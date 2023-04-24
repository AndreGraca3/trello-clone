package pt.isel.ls.server.data.dataMem

import pt.isel.ls.server.data.dataInterfaces.UserData
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.User
import java.util.*

class UserDataMem : UserData {

    val users = mutableListOf<User>(User(1, "alberto.tremocos@gmail.com", "Jose", "token123", "https://live.staticflickr.com/65535/52841364369_13521f6ef1_m.jpg"))

    override val size get() = users.size

    override fun createUser(name: String, email: String): Pair<Int, String> {
        val token = UUID.randomUUID().toString()
        val newUser = User(getNextId(), email, name, token)
        users.add(newUser)
        return Pair(newUser.idUser, token)
    }

    override fun getUser(token: String): User {
        return users.find { it.token == token } ?: throw TrelloException.NotAuthorized()
    }

    override fun getUser(idUser: Int): User {
        return users.find { it.idUser == idUser } ?: throw TrelloException.NotFound("User")
    }

    override fun checkEmail(email: String) {
        if (users.any { it.email == email }) throw TrelloException.AlreadyExists(email)
    }

    override fun getUsers(idUsers: List<Int>, limit: Int, skip: Int): List<User> {
        return users.filter { idUsers.contains(it.idUser) }.subList(skip, limit)
    }

    private fun getNextId(): Int {
        return if (users.isEmpty()) 0 else users.last().idUser + 1
    }

    override fun changeAvatar(idUser: Int, avatar: String) {
        val user = getUser(idUser)
        users[users.indexOf(user)] = user.copy(avatar = avatar)
    }
}
