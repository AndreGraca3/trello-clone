package pt.isel.ls.server.data.dataMem.models

import pt.isel.ls.server.data.transactionManager.transactions.TransactionCtx
import pt.isel.ls.server.data.dataInterfaces.models.BoardData
import pt.isel.ls.server.data.dataMem.boards
import pt.isel.ls.server.data.dataMem.lists
import pt.isel.ls.server.data.dataMem.usersBoards
import pt.isel.ls.server.exceptions.ALREADY_EXISTS
import pt.isel.ls.server.exceptions.INVAL_PARAM
import pt.isel.ls.server.exceptions.NOT_FOUND
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.Board
import pt.isel.ls.server.BoardWithLists
import java.sql.SQLException
import kotlin.math.min

class BoardDataMem : BoardData {

    override fun createBoard(idUser: Int, name: String, description: String, ctx: TransactionCtx): Int {
        if (name.length > 20) throw SQLException("$INVAL_PARAM name is too long.", "22001")
        if (boards.any { it.name == name }) throw SQLException("Board $name $ALREADY_EXISTS", "23505")
        val newBoard = Board(getNextId(), name, description)
        boards.add(newBoard)
        return newBoard.idBoard
    }

    override fun getBoard(idBoard: Int, ctx: TransactionCtx): Board {
        return boards.find { it.idBoard == idBoard } ?: throw TrelloException.NotFound("Board $NOT_FOUND")
    }

    override fun getBoardsFromUser(
        idUser: Int,
        limit: Int?,
        skip: Int?,
        name: String,
        numLists: Int?,
        ctx: TransactionCtx
    ): List<BoardWithLists> {
        val max = usersBoards.filter { it.idUser == idUser }.size
        val paging = checkPaging(max, limit, skip)
        val filtered = usersBoards.filter { it.idUser == idUser }.subList(paging.first, paging.second)
        val mapped = filtered.map {
            val board = getBoard(it.idBoard, ctx)
            BoardWithLists(
                it.idBoard,
                board.name,
                board.description,
                board.primaryColor,
                board.secondaryColor,
                lists.count { list -> list.idBoard == it.idBoard }
            )
        }
        return mapped.filter { b -> b.numLists == (numLists ?: b.numLists) && b.name.contains(name) }
    }

    private fun getNextId(): Int {
        return if (boards.isEmpty()) 1 else boards.last().idBoard + 1
    }

    private fun checkPaging(max: Int, limit: Int?, skip: Int?): Pair<Int, Int> {
        val skipped = if (skip == null || skip < 0) 0 else min(skip, max)
        var limited = if (limit == null || limit < 0 || skipped + limit > max) max else skipped + limit
        if (skipped > limited) limited = skipped
        return Pair(skipped, limited)
    }
}
