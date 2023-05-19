package pt.isel.ls.server.data.dataInterfaces

import pt.isel.ls.server.utils.BoardSQL
import pt.isel.ls.server.utils.BoardWithLists

interface BoardData {
    val size: Int

    fun createBoard(idUser: Int, name: String, description: String): Int

    fun getBoard(idBoard: Int): List<BoardSQL>

    fun checkBoardName(name: String)

    fun getBoardsFromUser(idUser: Int, limit: Int?, skip: Int?): List<BoardWithLists>
}
