package pt.isel.ls.server.data.dataInterfaces.models

import pt.isel.ls.server.UserProfile
import pt.isel.ls.server.data.transactionManager.transactions.TransactionCtx

interface UserBoardData {

    fun addUserToBoard(idUser: Int, idBoard: Int, ctx: TransactionCtx): UserProfile

    fun searchUserBoards(idUser: Int, ctx: TransactionCtx): List<Int> // can be deleted, it's not being used.

    fun checkUserInBoard(idUser: Int, idBoard: Int, ctx: TransactionCtx)

    fun getIdUsersFromBoard(idBoard: Int, ctx: TransactionCtx): List<Int> // can be deleted, it's not being used.

    fun getBoardCountFromUser(idUser: Int, name: String, numLists: Int?, ctx: TransactionCtx): Int

    fun getUserCountFromBoard(idBoard: Int, ctx: TransactionCtx): Int // can be deleted, it's not being used.
}
