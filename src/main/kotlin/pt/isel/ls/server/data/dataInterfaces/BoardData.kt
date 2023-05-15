package pt.isel.ls.server.data.dataInterfaces

import pt.isel.ls.server.utils.Board
import pt.isel.ls.server.utils.BoardWithLists

interface BoardData {
    val size: Int

    fun createBoard(idUser: Int, name: String, description: String): Int

    fun getBoard(idBoard: Int): Board

    fun checkBoardName(name: String)

    fun getBoardsFromUser(idBoards: List<Int>, limit: Int, skip: Int): List<BoardWithLists>
}
