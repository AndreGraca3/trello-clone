package pt.isel.ls.server.data.dataInterfaces.models

import pt.isel.ls.server.data.transactionManager.transaction.ITransactionContext
import pt.isel.ls.server.utils.Board
import pt.isel.ls.server.utils.BoardWithLists

interface BoardData {

    fun createBoard(idUser: Int, name: String, description: String, ctx: ITransactionContext): Int

    fun getBoard(idBoard: Int, ctx: ITransactionContext): Board

    fun getBoardsFromUser(
        idUser: Int,
        limit: Int?,
        skip: Int?,
        name: String,
        numLists: Int?,
        ctx: ITransactionContext
    ): List<BoardWithLists>
}
