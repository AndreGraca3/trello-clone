package pt.isel.ls.server.data.dataMem

import pt.isel.ls.server.data.dataInterfaces.ListData
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.BoardList
import pt.isel.ls.server.utils.checkPaging
import kotlin.math.min

class ListDataMem : ListData {
    //  Board(0, "Board1", "this is description1"), Board(1, "Board2", "this is description2")
    val lists = mutableListOf<BoardList>(
        BoardList(0, 0, "List0"),
        BoardList(1, 0, "List1"),
        BoardList(2, 0, "List2"),
        BoardList(3, 0, "List3"),
        BoardList(4, 0, "List4"),
        BoardList(5, 0, "List5"),
        BoardList(6, 0, "List6"),
        BoardList(7, 0, "List7"),
        BoardList(8, 0, "List8"),
    )


    override val size get() = lists.size

    override fun createList(idBoard: Int, name: String): Int {
        val newBoardList = BoardList(getNextId(), idBoard, name)
        lists.add(newBoardList)
        return newBoardList.idList
    }

    override fun getList(idList: Int, idBoard: Int): BoardList {
        return lists.find { it.idList == idList && it.idBoard == idBoard }
            ?: throw TrelloException.NotFound("BoardList")
    }

    override fun getListsOfBoard(idBoard: Int, limit: Int, skip: Int): List<BoardList> {
        return lists.filter { it.idBoard == idBoard }.subList(skip, skip + limit)
    }

    override fun checkListInBoard(idList: Int, idBoard: Int): BoardList {
        return lists.find { it.idBoard == idBoard && it.idList == idList } ?: throw TrelloException.NotFound("List")
    }

    override fun deleteList(idList: Int, idBoard: Int) {
        if(!lists.removeIf { it.idList == idList && it.idBoard == idBoard }) throw TrelloException.NoContent("List")
    }

    override fun getListCount(idBoard: Int): Int {
        return lists.count { it.idBoard == idBoard }
    }

    private fun getNextId(): Int {
        return if (lists.isEmpty()) 0 else lists.last().idList + 1
    }
}
