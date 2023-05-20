package pt.isel.ls.server.data.dataMem

import pt.isel.ls.server.data.dataInterfaces.BoardData
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.Board
import pt.isel.ls.server.utils.BoardSQL
import pt.isel.ls.server.utils.BoardWithLists

class BoardDataMem : BoardData {

    val boards =
        mutableListOf<Board>()

    override val size get() = boards.size

    override fun createBoard(idUser: Int, name: String, description: String): Int {
        val newBoard = Board(getNextId(), name, description)
        boards.add(newBoard)
        return newBoard.idBoard
    }

    override fun getBoard(idBoard: Int): List<BoardSQL> {
        //return boards.find { it.idBoard == idBoard } ?: throw TrelloException.NotFound("Board")
        TODO("Not yet implemented!")
    }

    override fun getBoardsFromUser(idUser: Int,limit: Int?,skip: Int?,name: String,numLists: Int): List<BoardWithLists> {
        //return boards.filter { idBoards.contains(it.idBoard) }.subList(skip, skip + limit).map { BoardWithLists(it.idBoard, it.name, it.description, 0) }
        TODO("Not yet implemented!")
    }

    private fun getNextId(): Int {
        return if (boards.isEmpty()) 1 else boards.last().idBoard + 1
    }
}
