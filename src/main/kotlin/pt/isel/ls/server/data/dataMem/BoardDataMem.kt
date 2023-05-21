package pt.isel.ls.server.data.dataMem

import pt.isel.ls.server.data.dataInterfaces.models.BoardData
import pt.isel.ls.server.utils.Board
import pt.isel.ls.server.utils.BoardWithLists
import java.sql.Connection

class BoardDataMem : BoardData {

    val boards =
        mutableListOf<Board>()

    override fun createBoard(idUser: Int, name: String, description: String, con: Connection): Int {
        val newBoard = Board(getNextId(), name, description)
        boards.add(newBoard)
        return newBoard.idBoard
    }

    override fun getBoard(idBoard: Int, con: Connection): Board {
        //return boards.find { it.idBoard == idBoard } ?: throw TrelloException.NotFound("Board")
        TODO("Not yet implemented!")
    }

    override fun getBoardsFromUser(
        idUser: Int,
        limit: Int?,
        skip: Int?,
        name: String,
        numLists: Int,
        con: Connection
    ): List<BoardWithLists> {
        //return boards.filter { idBoards.contains(it.idBoard) }.subList(skip, skip + limit).map { BoardWithLists(it.idBoard, it.name, it.description, 0) }
        TODO("Not yet implemented")
    }

    private fun getNextId(): Int {
        return if (boards.isEmpty()) 1 else boards.last().idBoard + 1
    }
}