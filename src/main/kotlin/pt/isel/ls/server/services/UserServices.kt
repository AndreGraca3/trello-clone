package pt.isel.ls.server.services

import pt.isel.ls.server.data.userData.DataUser
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.User
import pt.isel.ls.server.utils.isValidString

class UserServices(private val userData: DataUser) {

    /** ------------------------------ *
     *         User Management         *
     *  ---------------------------- **/

    fun createUser(name: String, email: String): Pair<Int, String> {
        isValidString(name)
        isValidString(email)
        if (!Regex("@").containsMatchIn(email)) throw TrelloException.IllegalArgument(email)
        if (!userData.checkEmail(email)) throw TrelloException.AlreadyExists(email)
        return userData.createUser(name, email)
    }

    fun getUser(token: String): User { /** this should be in data.**/
        return userData.getUser(token) ?: throw TrelloException.NotAuthorized() // not sure
    }

    fun getUser(id: Int): User { /** this should be in data.**/
        return userData.getUser(id) ?: throw TrelloException.NotFound("User")
    }
}