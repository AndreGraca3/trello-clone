package pt.isel.ls.server.data.dataPostGres.dataSQL.models

import pt.isel.ls.server.data.transactionManager.transactions.TransactionCtx
import pt.isel.ls.server.data.transactionManager.transactions.SQLTransaction
import pt.isel.ls.server.data.dataInterfaces.models.BoardData
import pt.isel.ls.server.data.dataPostGres.statements.BoardStatements
import pt.isel.ls.server.exceptions.NOT_FOUND
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.Board
import pt.isel.ls.server.utils.BoardWithLists

class BoardDataSQL : BoardData {

    override fun createBoard(idUser: Int, name: String, description: String, ctx: TransactionCtx): Int {
        val insertStmtBoard = BoardStatements.createBoardCMD(name, description)
        val idBoard: Int

        val res = (ctx as SQLTransaction).con.prepareStatement(insertStmtBoard).executeQuery()
        res.next()

        idBoard = res.getInt("idBoard")

        return idBoard
    }

    override fun getBoard(idBoard: Int, ctx: TransactionCtx): Board {
        val selectStmt = BoardStatements.getBoardCMD(idBoard)

        val res = (ctx as SQLTransaction).con.prepareStatement(selectStmt).executeQuery()
        res.next()

        if (res.row == 0) throw TrelloException.NotFound("Board $NOT_FOUND")

        return Board(idBoard, res.getString("name"), res.getString("description"))
    }

    override fun getBoardsFromUser(
        idUser: Int,
        limit: Int?,
        skip: Int?,
        name: String,
        numLists: Int?,
        ctx: TransactionCtx
    ): List<BoardWithLists> {
        val boards = mutableListOf<BoardWithLists>()
        val selectStmt = BoardStatements.getBoardsFromUser(idUser, limit, skip, name, numLists)

        val res = (ctx as SQLTransaction).con.prepareStatement(selectStmt).executeQuery()

        while (res.next()) {
            if (res.row == 0) return emptyList()
            boards.add(
                BoardWithLists(
                    res.getInt("idBoard"),
                    res.getString("name"),
                    res.getString("description"),
                    res.getInt("numLists")
                )
            )
        }
        return boards
    }
}
