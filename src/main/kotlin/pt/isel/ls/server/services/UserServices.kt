package pt.isel.ls.server.services

import pt.isel.ls.server.data.dataInterfaces.DataExecutor
import pt.isel.ls.server.data.dataInterfaces.models.UserData
import pt.isel.ls.server.exceptions.INVAL_PARAM
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.exceptions.map
import pt.isel.ls.server.utils.User
import pt.isel.ls.server.utils.isValidString
import java.sql.SQLException

class UserServices(private val userData: UserData, private val dataExecutor: DataExecutor<Any>) {

    fun createUser(name: String, email: String): Pair<Int, String> {
        isValidString(name, "name")
        isValidString(email, "email")
        if (!Regex("@").containsMatchIn(email)) throw TrelloException.IllegalArgument("$INVAL_PARAM $email")

        return dataExecutor.execute {
            userData.createUser(name, email, it)
        } as Pair<Int, String>
    }

    fun getUser(token: String): User {
        return dataExecutor.execute { userData.getUser(token, it) } as User
    }

    fun changeAvatar(token: String, avatar: String) {
        isValidString(avatar, "avatar")
        return dataExecutor.execute {
            userData.getUser(token, it)
            userData.changeAvatar(token, avatar, it) // verify if this throws a SQLException in some situation.
        } as Unit
    }
}
