package pt.isel.ls.server.services

import pt.isel.ls.server.data.dataInterfaces.UserData
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.User
import pt.isel.ls.server.utils.isValidString

class UserServices(private val userData: UserData) {

    /** ------------------------------ *
     *         User Management         *
     *  ---------------------------- **/

    fun createUser(name: String, email: String): Pair<Int, String> {
        isValidString(name, "name")
        isValidString(email, "email")
        if (!Regex("@").containsMatchIn(email)) throw TrelloException.IllegalArgument(email)
        userData.checkEmail(email)
        return userData.createUser(name, email)
    }

    fun getUser(token: String): User {
        return userData.getUser(token)
    }
}