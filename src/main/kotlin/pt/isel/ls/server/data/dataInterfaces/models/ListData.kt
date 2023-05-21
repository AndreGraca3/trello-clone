package pt.isel.ls.server.data.dataInterfaces.models

import pt.isel.ls.server.utils.BoardList
import java.sql.Connection

interface ListData {

    fun createList(idBoard: Int, name: String, con: Connection): Int

    fun getList(idList: Int, idBoard: Int, con: Connection): BoardList

    fun getListsOfBoard(idBoard: Int, con: Connection): List<BoardList>

    fun deleteList(idList: Int, idBoard: Int, con: Connection)

    fun getListCount(idBoard: Int, con: Connection): Int
}
