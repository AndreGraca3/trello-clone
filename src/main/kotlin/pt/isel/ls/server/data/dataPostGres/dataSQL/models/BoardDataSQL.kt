package pt.isel.ls.server.data.dataPostGres.dataSQL.models

import pt.isel.ls.server.data.dataInterfaces.models.BoardData
import pt.isel.ls.server.data.dataPostGres.statements.BoardStatements
import pt.isel.ls.server.utils.Board
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

    override fun getBoard(idBoard: Int, con: Connection): Board {
        val selectStmt = BoardStatements.getBoardCMD(idBoard)

        val res = con.prepareStatement(selectStmt).executeQuery()
        res.next()

        return Board(idBoard, res.getString("name"), res.getString("description"))
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