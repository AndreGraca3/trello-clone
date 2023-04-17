package pt.isel.ls.server.data.dataPostGres.dataSQL

import pt.isel.ls.server.data.dataInterfaces.BoardData
import pt.isel.ls.server.data.dataPostGres.statements.BoardStatements
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.Board
import pt.isel.ls.server.utils.setup

class BoardDataSQL : BoardData {

    override val size: Int = 0

    override fun createBoard(idUser: Int, name: String, description: String): Int {
        /** Not sure if I like this! **/
        val dataSource = setup()
        val insertStmtBoard = BoardStatements.createBoardCMD(name, description)
        var idBoard: Int
        val selectStmt = BoardStatements.getBoardByNameCMD(name)

        dataSource.connection.use {
            it.autoCommit = false
            it.prepareStatement(insertStmtBoard).executeUpdate()

            val res = it.prepareStatement(selectStmt).executeQuery()
            res.next()

            idBoard = res.getInt("idBoard")
            val insertStmtUser = BoardStatements.addUserToBoard(idUser, idBoard)

            it.prepareStatement(insertStmtUser).executeUpdate()

            it.autoCommit = true
        }
        return idBoard
    }

    override fun getBoard(idBoard: Int): Board {
        val dataSource = setup()
        val selectStmt = BoardStatements.getBoardCMD(idBoard)
        lateinit var name: String
        lateinit var description: String

        dataSource.connection.use {
            it.autoCommit = false
            val res = it.prepareStatement(selectStmt).executeQuery()
            res.next()

            if (res.row == 0) throw TrelloException.NotFound("Board")

            name = res.getString("name")
            description = res.getString("description")

            it.autoCommit = true
        }
        return Board(idBoard, name, description)
    }

    override fun checkBoardName(name: String) {
        val dataSource = setup()
        val selectStmt = BoardStatements.getBoardByNameCMD(name)

        dataSource.connection.use {
            it.autoCommit = false
            val res = it.prepareStatement(selectStmt).executeQuery()
            res.next()

            if (res.row != 0) throw throw TrelloException.AlreadyExists(name)

            it.autoCommit = true
        }
    }

    override fun getBoardsFromUser(idBoards: List<Int>, limit: Int?, skip: Int?): List<Board> {
        val dataSource = setup()
        val selectStmt = BoardStatements.getBoardsFromUser(3)
        val boards = mutableListOf<Board>()

        dataSource.connection.use {
            it.autoCommit = false
            val res = it.prepareStatement(selectStmt).executeQuery()

            while (res.next()) {
                if (res.row == 0) return emptyList()
                boards.add(
                    Board(
                        res.getInt("idBoard"),
                        res.getString("name"),
                        res.getString("description")
                    )
                )
            }
        }
        return boards
    }
}

/** Reunir quando der sobre isto !**/
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
