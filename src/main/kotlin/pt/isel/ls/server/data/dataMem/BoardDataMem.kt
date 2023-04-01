package pt.isel.ls.server.data.dataMem

import pt.isel.ls.server.data.dataInterfaces.BoardData
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.*

class BoardDataMem : BoardData {

    val boards = mutableListOf<Board>()

    override fun createBoard(idUser: Int, name: String, description: String): Int {
        val newBoard = Board(getNextId(), name, description)
        boards.add(newBoard)
        return newBoard.idBoard
    }

    override fun getBoard(idBoard: Int): Board {
        return boards.find { it.idBoard == idBoard } ?: throw TrelloException.NotFound("Board")
    }

    override fun checkBoardName(name: String) {
        if(boards.any { it.name == name }) throw TrelloException.AlreadyExists("Board")
    }

    override fun getBoardsFromUser(idBoards: List<Int>): List<Board> {
        return boards.filter { idBoards.contains(it.idBoard) }
    }

    private fun getNextId(): Int {
        return boards.last().idBoard + 1
    }
}