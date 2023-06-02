package pt.isel.ls.server.services

import pt.isel.ls.server.data.transactionManager.executor.DataExecutor
import pt.isel.ls.server.data.dataInterfaces.models.UserData
import pt.isel.ls.server.User
import pt.isel.ls.server.exceptions.INVAL_PARAM
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.security.hashPassword
import pt.isel.ls.server.utils.validateString

class UserServices(private val userData: UserData, private val dataExecutor: DataExecutor) {

    fun createUser(name: String, email: String, password: String, urlAvatar: String?): Pair<Int, String> {
        validateString(name, "name")
        validateString(email, "email")
        verifyEmail(email)

        return dataExecutor.execute {
            userData.createUser(name, email, hashPassword(password), urlAvatar, it)
        }
    }

    fun getUser(token: String): User {
        return dataExecutor.execute { userData.getUser(token, it) }
    }

    fun changeAvatar(token: String, avatar: String) {
        return dataExecutor.execute {
            userData.getUser(token, it)
            userData.changeAvatar(token, avatar, it)
        }
    }

    fun login(email: String, password: String): String {
        validateString(email, "email")
        verifyEmail(email)
        validateString(password, "password")
        return dataExecutor.execute {
            userData.login(email, hashPassword(password), it)
        }
    }

    private fun verifyEmail(email: String) {
        if (!Regex("@").containsMatchIn(email)) throw TrelloException.IllegalArgument("$INVAL_PARAM $email")
    }
}
