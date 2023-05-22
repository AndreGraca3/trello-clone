package pt.isel.ls.server.data.dataInterfaces.models

import pt.isel.ls.server.data.transactionManager.transaction.ITransactionContext
import pt.isel.ls.server.utils.BoardList

interface ListData {

    fun createList(idBoard: Int, name: String, ctx: ITransactionContext): Int

    fun getList(idList: Int, idBoard: Int, ctx: ITransactionContext): BoardList

    fun getListsOfBoard(idBoard: Int, ctx: ITransactionContext): List<BoardList>

    fun deleteList(idList: Int, idBoard: Int, ctx: ITransactionContext)

    fun getListCount(idBoard: Int, ctx: ITransactionContext): Int // can be deleted, it's not being used.
}
