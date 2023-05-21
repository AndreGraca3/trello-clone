package pt.isel.ls.server.data.dataInterfaces.models

import pt.isel.ls.server.utils.Board
import pt.isel.ls.server.utils.BoardWithLists
import java.sql.Connection

interface BoardData {

    fun createBoard(idUser: Int, name: String, description: String, con: Connection): Int

    fun getBoard(idBoard: Int, con: Connection): Board

    fun getBoardsFromUser(
        idUser: Int,
        limit: Int?,
        skip: Int?,
        name: String,
        numLists: Int?,
        con: Connection
    ): List<BoardWithLists>
}
