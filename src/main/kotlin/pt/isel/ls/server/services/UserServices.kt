package pt.isel.ls.server.services

import pt.isel.ls.server.data.transactionManager.executor.DataExecutor
import pt.isel.ls.server.data.dataInterfaces.models.UserData
import pt.isel.ls.server.exceptions.INVAL_PARAM
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.User
import pt.isel.ls.server.utils.hashPassword
import pt.isel.ls.server.utils.isValidAvatar
import pt.isel.ls.server.utils.isValidString

class UserServices(private val userData: UserData, private val dataExecutor: DataExecutor) {

    fun createUser(name: String, email: String, password: String, urlAvatar: String?): Pair<Int, String> {
        isValidString(name, "name")
        isValidString(email, "email")
        if (!Regex("@").containsMatchIn(email)) throw TrelloException.IllegalArgument("$INVAL_PARAM $email")

        return dataExecutor.execute {
            userData.createUser(name, email, hashPassword(password), isValidAvatar(urlAvatar), it)
        }
    }
    fun getUser(token: String): User {
        return dataExecutor.execute { userData.getUser(token, it) }
    }

    fun changeAvatar(token: String, avatar: String) {
        isValidString(avatar, "avatar")
        return dataExecutor.execute {
            userData.getUser(token, it)
            userData.changeAvatar(token, avatar, it) // verify if this throws a SQLException in some situation.
        }
    }

    fun login(email: String, password: String): String {
        isValidString(email, "email")
        isValidString(password, "password")
        return dataExecutor.execute {
            userData.login(email, hashPassword(password), it)
        }
    }
}
