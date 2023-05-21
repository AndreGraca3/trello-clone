package pt.isel.ls.server.data.dataMem.models

import pt.isel.ls.server.data.dataInterfaces.models.BoardData
import pt.isel.ls.server.data.dataMem.boards
import pt.isel.ls.server.data.dataMem.lists
import pt.isel.ls.server.data.dataMem.usersBoards
import pt.isel.ls.server.exceptions.ALREADY_EXISTS
import pt.isel.ls.server.exceptions.NOT_FOUND
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.Board
import pt.isel.ls.server.utils.BoardSQL
import pt.isel.ls.server.utils.BoardWithLists
import pt.isel.ls.server.utils.checkPaging
import java.sql.Connection
import java.sql.SQLException

class BoardDataMem : BoardData {

    override fun createBoard(idUser: Int, name: String, description: String, con: Connection): Int {
        if(boards.any { it.name == name }) throw SQLException("Board $name $ALREADY_EXISTS", "23505")
        val newBoard = Board(getNextId(), name, description)
        boards.add(newBoard)
        return newBoard.idBoard
    }

    override fun getBoard(idBoard: Int, con: Connection): Board {
        return boards.find { it.idBoard == idBoard } ?: throw TrelloException.NotFound("Board $NOT_FOUND")
    }

    override fun getBoardsFromUser(
        idUser: Int,
        limit: Int?,
        skip: Int?,
        name: String,
        numLists: Int?,
        con: Connection
    ): List<BoardWithLists> {
        val max = usersBoards.filter { it.idUser == idUser }.size
        val paging = checkPaging(max, limit, skip)
        return usersBoards.filter { it.idUser == idUser }.subList(paging.first,paging.second)
            .map {
                BoardWithLists(
                    it.idBoard,
                    boards.find { board -> board.idBoard == it.idBoard }!!.name,
                    boards.find { board -> board.idBoard == it.idBoard }!!.description,
                    lists.count { list -> list.idBoard == it.idBoard }
                )
            }.also {
                if(numLists != null) it.filter { board -> board.numLists == numLists }
            }
    }

    private fun getNextId(): Int {
        return if (boards.isEmpty()) 1 else boards.last().idBoard + 1
    }
}
