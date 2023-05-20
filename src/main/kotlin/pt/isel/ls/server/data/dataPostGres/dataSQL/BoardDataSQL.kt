package pt.isel.ls.server.data.dataPostGres.dataSQL

import pt.isel.ls.server.data.dataInterfaces.BoardData
import pt.isel.ls.server.data.dataPostGres.statements.BoardStatements
import pt.isel.ls.server.data.dataPostGres.statements.UserBoardStatements
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.BoardSQL
import pt.isel.ls.server.utils.BoardWithLists
import pt.isel.ls.server.utils.TotalBoards
import pt.isel.ls.server.utils.setup

class BoardDataSQL : BoardData {

    override val size: Int get() = getSizeCount("idBoard", "board")

    override fun createBoard(idUser: Int, name: String, description: String): Int {
        val dataSource = setup()
        val insertStmtBoard = BoardStatements.createBoardCMD(name, description)
        var idBoard: Int

        dataSource.connection.use {
            it.autoCommit = false
            val res = it.prepareStatement(insertStmtBoard).executeQuery()
            res.next()

            idBoard = res.getInt("idBoard")

            it.prepareStatement(
                UserBoardStatements.addUserToBoard(idUser, idBoard)
            ).executeUpdate()

            it.autoCommit = true
        }
        return idBoard
    }

    override fun getBoard(idBoard: Int): List<BoardSQL> {
        val dataSource = setup()
        val selectStmt = BoardStatements.getBoardCMD(idBoard)
        val listBoardSQL = mutableListOf<BoardSQL>()

        dataSource.connection.use {
            it.autoCommit = false

            val res = it.prepareStatement(selectStmt).executeQuery()

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
            it.autoCommit = true
        }
        return listBoardSQL
    }

    override fun getBoardsFromUser(
        idUser: Int,
        limit: Int?,
        skip: Int?,
        name: String,
        numLists: Int
    ): List<BoardWithLists> {

        val boards = mutableListOf<BoardWithLists>()
        val dataSource = setup()
        val selectStmt = BoardStatements.getBoardsFromUser(idUser, limit, skip, name, numLists)

        dataSource.connection.use {
            it.autoCommit = false
            val res = it.prepareStatement(selectStmt).executeQuery()

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
            it.autoCommit = true
        }
        return boards
    }
}

/** Reunir quando der sobre isto ! **/
/*     override fun getBoardsFromUser(idUser: Int): List<Board> {
        val dataSource = setup()
        val selectStmt = BoardStatements.getBoardsFromUser(idUser)
        val idBoards = mutableListOf<Int>()
        val names = mutableListOf<String>()
        val descriptions = mutableListOf<String>()

        dataSource.connection.use {
            it.autoCommit = false
            val res = it.prepareStatement(selectStmt).executeQuery()
            res.next()

            if(res.row == 0) return emptyList()
            idBoards.add(res.getInt("idBoard"))
            names.add(res.getString("name"))
            descriptions.add(res.getString("description"))

            while(res.next()){
                idBoards.add(res.getInt("idBoard"))
                names.add(res.getString("name"))
                descriptions.add(res.getString("description"))
            }
        }
        val boards = mutableListOf<Board>()
        (0..idBoards.size).forEach { i ->
            boards.add(Board(idBoards[i],names[i],descriptions[i]))
        }
        return boards
    }*/
