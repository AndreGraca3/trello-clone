package pt.isel.ls.server.data.dataInterfaces

import pt.isel.ls.server.utils.Board

interface BoardData {
    fun createBoard(idUser: Int, name: String, description: String): Int

    fun getBoard(idBoard: Int): Board

    fun getBoardByName(name: String): Board

    fun getBoardsFromUser(idUser: Int): List<Board>

    fun addUserToBoard(idUser: Int, idBoard: Int)
}
