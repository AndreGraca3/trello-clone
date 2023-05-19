package pt.isel.ls.server.services

import pt.isel.ls.server.data.dataInterfaces.UserData
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.exceptions.map
import pt.isel.ls.server.utils.User
import pt.isel.ls.server.utils.isValidString
import java.sql.SQLException

class UserServices(private val userData: UserData) {

    /** ------------------------------ *
     *         User Management         *
     *  ---------------------------- **/

    fun createUser(name: String, email: String): Pair<Int, String> {
        isValidString(name, "name")
        isValidString(email, "email")
        if (!Regex("@").containsMatchIn(email)) throw TrelloException.IllegalArgument(email)
        // userData.checkEmail(email)
        try {
            return userData.createUser(name, email)
        } catch (ex: SQLException) {
            val trelloException = map[ex.sqlState] ?: throw Exception()
            throw trelloException("email")
        }
    }

    fun getUser(token: String): User {
        return userData.getUser(token)
    }

    fun changeAvatar(token: String, avatar: String) {
        isValidString(avatar, "avatar")
        userData.getUser(token)
        userData.changeAvatar(token, avatar) // verify if this throws a SQLException in some situation.
    }
}
