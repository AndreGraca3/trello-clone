package pt.isel.ls.server.data.dataMem

import pt.isel.ls.server.utils.BoardList
import pt.isel.ls.server.data.dataInterfaces.ListData
import pt.isel.ls.server.exceptions.TrelloException

class ListDataMem : ListData {

    val lists = mutableListOf<BoardList>()

    override fun createList(idBoard: Int, name: String): Int {
        val newBoardList = BoardList(getNextId(), idBoard, name)
        lists.add(newBoardList)
        return newBoardList.idList
    }

    override fun getList(idList: Int, idBoard: Int): BoardList {
        return lists.find { it.idList == idList && it.idBoard == idBoard} ?: throw TrelloException.NotFound("BoardList")
    }

    override fun getListsOfBoard(idBoard: Int): List<BoardList> {
        return lists.filter { it.idBoard == idBoard }
    }

    override fun checkListInBoard(idList: Int, idBoard: Int) : BoardList {
        return lists.find { it.idBoard == idBoard && it.idList == idList } ?: throw TrelloException.NotFound("Board")
    }

    private fun getNextId() : Int {
        return if(lists.isEmpty()) 0 else lists.last().idBoard + 1
    }
}
