package pt.isel.ls.server.data.dataPostGres.dataSQL.models

import pt.isel.ls.server.data.dataInterfaces.models.BoardData
import pt.isel.ls.server.data.dataPostGres.statements.BoardStatements
import pt.isel.ls.server.utils.BoardSQL
import pt.isel.ls.server.utils.BoardWithLists
import java.sql.Connection

class BoardDataSQL : BoardData {

    override fun createBoard(idUser: Int, name: String, description: String, con: Connection): Int {
        val insertStmtBoard = BoardStatements.createBoardCMD(name, description)
        val idBoard: Int

        val res = con.prepareStatement(insertStmtBoard).executeQuery()
        res.next()

        idBoard = res.getInt("idBoard")

        return idBoard
    }

    override fun getBoard(idBoard: Int, con: Connection): List<BoardSQL> {
        val selectStmt = BoardStatements.getBoardCMD(idBoard)
        val listBoardSQL = mutableListOf<BoardSQL>()

        val res = con.prepareStatement(selectStmt).executeQuery()

        while (res.next()) {
            listBoardSQL.add(
                BoardSQL(
                    res.getString("name"),
                    res.getString("description"),
                    res.getInt("idList"),
                    res.getString("listName"),
                    if (res.getInt("idCard") == 0) null else res.getInt("idCard"),
                    res.getString("cardName"),
                    res.getInt("idx"),
                    res.getBoolean("archived")
                )
            )
        }
        return listBoardSQL
    }

    override fun getBoardsFromUser(
        idUser: Int,
        limit: Int?,
        skip: Int?,
        name: String,
        numLists: Int?,
        con: Connection
    ): List<BoardWithLists> {

        val boards = mutableListOf<BoardWithLists>()
        val selectStmt = BoardStatements.getBoardsFromUser(idUser, limit, skip, name, numLists!!)

        val res = con.prepareStatement(selectStmt).executeQuery()

        while (res.next()) {
            if (res.row == 0) return emptyList()
            /** Estamos a dar return antes de acabar a ligação!!! **/
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