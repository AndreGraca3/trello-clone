package pt.isel.ls.server.data.dataInterfaces

import pt.isel.ls.server.utils.Board

interface BoardData {
    fun createBoard(idUser: Int, name: String, description: String): Int

    fun getBoard(idBoard: Int): Board

    fun checkBoardName(name: String)

    fun getBoardsFromUser(idBoards : List<Int>): List<Board>
}
