package pt.isel.ls.server.data.dataInterfaces

import pt.isel.ls.server.utils.User

interface UserData {
    val size: Int

    fun createUser(name: String, email: String): Pair<Int, String>

    fun getUser(token: String): User

    fun getUser(idUser: Int): User

    fun checkEmail(email: String)

    fun getUsers(idUsers: List<Int>, limit: Int, skip: Int): List<User>

    fun changeAvatar(idUser: Int, avatar: String)
}
