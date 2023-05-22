package pt.isel.ls.server.data.dataInterfaces.models

import pt.isel.ls.server.data.transactionManager.transaction.ITransactionContext

interface UserBoardData {

    fun addUserToBoard(idUser: Int, idBoard: Int, ctx: ITransactionContext)

    fun searchUserBoards(idUser: Int): List<Int> // can be deleted, it's not being used.

    fun checkUserInBoard(idUser: Int, idBoard: Int, ctx: ITransactionContext)

    fun getIdUsersFromBoard(idBoard: Int): List<Int> // can be deleted, it's not being used.

    fun getBoardCountFromUser(idUser: Int, name: String, numLists: Int?): Int

    fun getUserCountFromBoard(idBoard: Int): Int // can be deleted, it's not being used.
}
