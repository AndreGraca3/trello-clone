package pt.isel.ls.server.data.listData

import pt.isel.ls.BoardList

interface IDataList {
    fun createList(idBoard: Int, name: String): Int

    fun getList(idList: Int): BoardList?

    fun getListsOfBoard(idBoard: Int): List<BoardList>
}
