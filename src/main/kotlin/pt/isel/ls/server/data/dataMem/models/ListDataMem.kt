package pt.isel.ls.server.data.dataMem.models

import pt.isel.ls.server.data.transactionManager.transaction.ITransactionContext
import pt.isel.ls.server.data.dataInterfaces.models.ListData
import pt.isel.ls.server.data.dataMem.lists
import pt.isel.ls.server.exceptions.INVAL_PARAM
import pt.isel.ls.server.exceptions.NOT_FOUND
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.BoardList
import java.sql.SQLException

class ListDataMem : ListData {

    override fun createList(idBoard: Int, name: String, ctx: ITransactionContext): Int {
        if (name.length > 20) throw SQLException("$INVAL_PARAM name is too long.", "22001")
        val newBoardList = BoardList(getNextId(), idBoard, name)
        lists.add(newBoardList)
        return newBoardList.idList
    }

    override fun getList(idList: Int, idBoard: Int, ctx: ITransactionContext): BoardList {
        return lists.find { it.idList == idList && it.idBoard == idBoard }
            ?: throw TrelloException.NotFound("List $NOT_FOUND")
    }

    override fun getListsOfBoard(idBoard: Int, ctx: ITransactionContext): List<BoardList> {
        return lists.filter { it.idBoard == idBoard }
    }

    override fun deleteList(idList: Int, idBoard: Int, ctx: ITransactionContext) {
        if (!lists.removeIf { it.idList == idList && it.idBoard == idBoard }) throw TrelloException.NoContent()
    }

    override fun getListCount(idBoard: Int, ctx: ITransactionContext): Int {
        return lists.count { it.idBoard == idBoard }
    }

    private fun getNextId(): Int {
        return if (lists.isEmpty()) 1 else lists.last().idList + 1
    }
}
