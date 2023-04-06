package pt.isel.ls.server.data.dataInterfaces

import pt.isel.ls.server.utils.BoardList

interface ListData {
    fun createList(idBoard: Int, name: String): Int

    fun getList(idList: Int, idBoard: Int): BoardList

    fun getListsOfBoard(idBoard: Int): List<BoardList>

    fun checkListInBoard(idList: Int, idBoard: Int): BoardList

    fun deleteList(idList: Int, idBoard: Int)
}
