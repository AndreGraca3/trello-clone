package pt.isel.ls.server.data.dataInterfaces.models

import pt.isel.ls.server.data.transactionManager.transactions.TransactionCtx
import pt.isel.ls.server.BoardList

interface ListData {

    fun createList(idBoard: Int, name: String, ctx: TransactionCtx): Int

    fun getList(idList: Int, idBoard: Int, ctx: TransactionCtx): BoardList

    fun getListsOfBoard(idBoard: Int, ctx: TransactionCtx): List<BoardList>

    fun deleteList(idList: Int, idBoard: Int, ctx: TransactionCtx)

    fun getListCount(idBoard: Int, ctx: TransactionCtx): Int // can be deleted, it's not being used.
}
