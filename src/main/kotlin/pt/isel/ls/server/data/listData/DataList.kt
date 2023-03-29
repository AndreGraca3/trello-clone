package pt.isel.ls.server.data.listData

import pt.isel.ls.server.utils.BoardList
import pt.isel.ls.server.data.getNextId
import pt.isel.ls.server.data.lists

class DataList : IDataList {
    override fun createList(idBoard: Int, name: String): Int {
        val newBoardList = BoardList(getNextId(BoardList::class.java), idBoard, name)
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
