package pt.isel.ls.server.data.dataInterfaces.models

import pt.isel.ls.server.data.transactionManager.transaction.ITransactionContext
import pt.isel.ls.server.utils.User

interface UserData {

    fun createUser(name: String, email: String, ctx: ITransactionContext): Pair<Int, String>

    fun getUser(token: String, ctx: ITransactionContext): User

    fun getUser(idUser: Int, ctx: ITransactionContext): User

    fun getUsers(idBoard: Int, limit: Int?, skip: Int?, ctx: ITransactionContext): List<User>

    fun changeAvatar(token: String, avatar: String, ctx: ITransactionContext)
}
