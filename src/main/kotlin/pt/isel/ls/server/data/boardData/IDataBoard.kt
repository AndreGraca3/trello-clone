package pt.isel.ls.server.data.boardData

import pt.isel.ls.server.utils.Board

interface IDataBoard {
    fun createBoard(idUser: Int, name: String, description: String): Int

    fun addUserToBoard(idUser: Int, board: Board)

    fun getBoardByName(name: String): Board?

    fun getBoardsFromUser(idUser: Int): List<Board>

    fun getBoard(idBoard: Int): Board?

    fun checkUserInBoard(idUser: Int, idBoard: Int): Boolean
}
