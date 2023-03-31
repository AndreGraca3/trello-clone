package pt.isel.ls.server.data.dataMem

import pt.isel.ls.server.data.dataInterfaces.BoardData
import pt.isel.ls.server.utils.Board
import pt.isel.ls.server.data.boards
import pt.isel.ls.server.data.getNextId
import pt.isel.ls.server.data.usersBoards
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.UserBoard

class BoardDataMem : BoardData {
    override fun createBoard(idUser: Int, name: String, description: String): Int {
        val newBoard = Board(getNextId(Board::class.java), name, description)
        addUserToBoard(idUser, newBoard.idBoard)
        boards.add(newBoard)
        return newBoard.idBoard
    }

    override fun getBoard(idBoard: Int): Board {
        return boards.find { it.idBoard == idBoard } ?: throw TrelloException.NotFound("Board")
    }

    override fun getBoardByName(name: String): Board {
        return boards.find { it.name == name } ?: throw TrelloException.AlreadyExists(name)
    }

    override fun addUserToBoard(idUser: Int, idBoard: Int) {
        usersBoards.add(UserBoard(idUser, idBoard))
    }

    override fun getBoardsFromUser(idUser: Int): List<Board> { /** does this make sence being here instead of services**/
        val idsBoard = usersBoards.filter { it.idUser == idUser }.map { it.idBoard }
        return boards.filter { idsBoard.contains(it.idBoard) }
    }
}