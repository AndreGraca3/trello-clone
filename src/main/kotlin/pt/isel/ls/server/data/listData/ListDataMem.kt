package pt.isel.ls.server.data.listData

import pt.isel.ls.Board
import pt.isel.ls.BoardList
import pt.isel.ls.server.getNextId

class ListDataMem : IDataList {
    private val lists = mutableListOf<BoardList>()


    override fun createList(idBoard: Int, name: String): Int {
        val newBoardList = BoardList(getNextId(lists), idBoard, name)
        lists.add(newBoardList)
        return newBoardList.idList
    }

    override fun getList(idList: Int): BoardList? {
        return lists.find { it.idList == idList }
    }

    override fun getListsOfBoard(idBoard: Int): List<BoardList> {
        return lists.filter { it.idBoard == idBoard }
    }
}
