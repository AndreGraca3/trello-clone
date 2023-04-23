package pt.isel.ls.server.data.dataInterfaces

import pt.isel.ls.server.utils.BoardList

interface ListData {
    val size: Int

    fun createList(idBoard: Int, name: String): Int

    fun getList(idList: Int, idBoard: Int): BoardList

    fun getListsOfBoard(idBoard: Int, limit: Int, skip: Int): List<BoardList>

    fun deleteList(idList: Int, idBoard: Int)

    fun getListCount(idBoard: Int) : Int
}
