package pt.isel.ls.server.data.dataInterfaces

import pt.isel.ls.server.utils.User

interface UserData {
    fun createUser(name: String, email: String): Pair<Int, String>

    fun getUser(token: String): User

    fun getUser(idUser: Int) : User

    fun checkEmail(email: String)
}
