package pt.isel.ls.server.data.boardData

import pt.isel.ls.Board
import pt.isel.ls.server.data.boards
import pt.isel.ls.server.data.getNextId

class DataBoard : IDataBoard {
    override fun createBoard(idUser: Int, name: String, description: String): Int {
        val newBoard = Board(getNextId(Board::class.java), name, description, mutableListOf())
        addUserToBoard(idUser, newBoard)
        boards.add(newBoard)
        return newBoard.idBoard
    }

    override fun getBoard(idBoard: Int): Board? {
        return boards.find { it.idBoard == idBoard }
    }

    override fun getBoardByName(name: String): Board? {
        return boards.find { it.name == name }
    }

    override fun addUserToBoard(idUser: Int, board: Board) {
        board.idUsers.add(idUser)
    }

    override fun getBoardsFromUser(idUser: Int): List<Board> {
        return boards.filter { it.idUsers.any { id -> id == idUser } }
    }

    override fun checkUserInBoard(idUser: Int, idBoard: Int): Boolean {
        return boards.any { it.idUsers.contains(idUser) && it.idBoard == idBoard }
    }

}