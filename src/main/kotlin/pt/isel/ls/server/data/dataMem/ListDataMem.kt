package pt.isel.ls.server.data.dataMem

import pt.isel.ls.server.utils.BoardList
import pt.isel.ls.server.data.getNextId
import pt.isel.ls.server.data.dataInterfaces.ListData
import pt.isel.ls.server.data.lists
import pt.isel.ls.server.exceptions.TrelloException

class ListDataMem : ListData {
    override fun createList(idBoard: Int, name: String): Int {
        val newBoardList = BoardList(getNextId(BoardList::class.java), idBoard, name)
        lists.add(newBoardList)
        return newBoardList.idList
    }

    override fun getList(idList: Int, idBoard: Int): BoardList {
        return lists.find { it.idList == idList && it.idBoard == idBoard} ?: throw TrelloException.NotFound("BoardList")
    }

    override fun getListsOfBoard(idBoard: Int): List<BoardList> {
        return lists.filter { it.idBoard == idBoard }
    }
}
