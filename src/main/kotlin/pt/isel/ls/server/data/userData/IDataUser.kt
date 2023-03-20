package pt.isel.ls.server.data.userData

import pt.isel.ls.User

interface IDataUser {
    fun createUser(name: String, email: String): Pair<Int, String>

    fun getUser(idUser: Int): User?

    fun checkEmail(email: String): Boolean

    fun getUser(token: String?): User?
}