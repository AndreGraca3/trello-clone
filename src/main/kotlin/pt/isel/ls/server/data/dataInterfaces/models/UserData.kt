package pt.isel.ls.server.data.dataInterfaces.models

import pt.isel.ls.server.utils.User
import java.sql.Connection

interface UserData {

    fun createUser(name: String, email: String, con: Connection): Pair<Int, String>

    fun getUser(token: String, con: Connection): User

    fun getUser(idUser: Int, con: Connection): User

    fun getUsers(idBoard: Int, limit: Int?, skip: Int?, con: Connection): List<User>

    fun changeAvatar(token: String, avatar: String, con: Connection)
}
