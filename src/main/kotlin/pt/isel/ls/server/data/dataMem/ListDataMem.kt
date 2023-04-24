package pt.isel.ls.server.data.dataMem

import pt.isel.ls.server.data.dataInterfaces.ListData
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.BoardList

class ListDataMem : ListData {

    val lists = mutableListOf<BoardList>()

    override val size get() = lists.size

    override fun createList(idBoard: Int, name: String): Int {
        val newBoardList = BoardList(getNextId(), idBoard, name)
        lists.add(newBoardList)
        return newBoardList.idList
    }

    override fun getList(idList: Int, idBoard: Int): BoardList {
        return lists.find { it.idList == idList && it.idBoard == idBoard }
            ?: throw TrelloException.NotFound("List")
    }

    override fun getListsOfBoard(idBoard: Int, limit: Int, skip: Int): List<BoardList> {
        return lists.filter { it.idBoard == idBoard }.subList(skip, limit)
    }

    override fun deleteList(idList: Int, idBoard: Int) {
        if (!lists.removeIf { it.idList == idList && it.idBoard == idBoard }) throw TrelloException.NoContent("List")
    }

    override fun getListCount(idBoard: Int): Int {
        return lists.count { it.idBoard == idBoard }
    }

    private fun getNextId(): Int {
        return if (lists.isEmpty()) 0 else lists.last().idList + 1
    }
}
