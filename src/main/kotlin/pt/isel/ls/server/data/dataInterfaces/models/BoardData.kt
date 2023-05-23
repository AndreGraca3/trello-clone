package pt.isel.ls.server.data.dataInterfaces.models

import pt.isel.ls.server.data.transactionManager.transactions.TransactionCtx
import pt.isel.ls.server.utils.Board
import pt.isel.ls.server.utils.BoardWithLists

interface BoardData {

    fun createBoard(idUser: Int, name: String, description: String, ctx: TransactionCtx): Int

    fun getBoard(idBoard: Int, ctx: TransactionCtx): Board

    fun getBoardsFromUser(
        idUser: Int,
        limit: Int?,
        skip: Int?,
        name: String,
        numLists: Int?,
        ctx: TransactionCtx
    ): List<BoardWithLists>
}
