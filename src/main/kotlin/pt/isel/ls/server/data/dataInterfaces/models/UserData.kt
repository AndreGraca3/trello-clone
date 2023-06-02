package pt.isel.ls.server.data.dataInterfaces.models

import pt.isel.ls.server.data.transactionManager.transactions.TransactionCtx
import pt.isel.ls.server.User

interface UserData {

    fun createUser(name: String, email: String, hashedPassword: String, urlAvatar: String?, ctx: TransactionCtx): Pair<Int, String>

    fun getUser(token: String, ctx: TransactionCtx): User

    fun getUser(idUser: Int, ctx: TransactionCtx): User

    fun getUsers(idBoard: Int, ctx: TransactionCtx): List<User>

    fun changeAvatar(token: String, avatar: String, ctx: TransactionCtx)

    fun login(email: String, hashedPassword: String, ctx: TransactionCtx) : String
}
