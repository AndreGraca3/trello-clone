package pt.isel.ls.server.data.dataPostGres.dataSQL.models

import pt.isel.ls.server.data.transactionManager.transactions.TransactionCtx
import pt.isel.ls.server.data.transactionManager.transactions.SQLTransaction
import pt.isel.ls.server.data.dataInterfaces.models.BoardData
import pt.isel.ls.server.data.dataPostGres.statements.BoardStatements
import pt.isel.ls.server.exceptions.NOT_FOUND
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.Board
import pt.isel.ls.server.BoardWithLists
import pt.isel.ls.server.utils.randomColor

class BoardDataSQL : BoardData {

    override fun createBoard(idUser: Int, name: String, description: String, ctx: TransactionCtx): Int {
        val insertStmtBoard = BoardStatements.createBoardCMD(name, description, randomColor(), randomColor())

        val res = (ctx as SQLTransaction).con.prepareStatement(insertStmtBoard).executeQuery()
        res.next()

        return res.getInt("idBoard")
    }

    override fun getBoard(idBoard: Int, ctx: TransactionCtx): Board {
        val selectStmt = BoardStatements.getBoardCMD(idBoard)

        val res = (ctx as SQLTransaction).con.prepareStatement(selectStmt).executeQuery()
        res.next()

        if (res.row == 0) throw TrelloException.NotFound("Board $NOT_FOUND")

        return Board(
            idBoard,
            res.getString("name"),
            res.getString("description"),
            res.getString("primaryColor"),
            res.getString("secondaryColor")
        )
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
                    res.getString("primaryColor"),
                    res.getString("secondaryColor"),
                    res.getInt("numLists")
                )
            )
        }
        return boards
    }
}
