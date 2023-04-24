package pt.isel.ls.server.data.dataPostGres.dataSQL

import pt.isel.ls.server.data.dataInterfaces.UserBoardData
import pt.isel.ls.server.data.dataPostGres.statements.BoardStatements
import pt.isel.ls.server.data.dataPostGres.statements.UserBoardStatements
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.setup

class UserBoardDataSQL : UserBoardData {

    override fun addUserToBoard(idUser: Int, idBoard: Int) {
        val dataSource = setup()
        val insertStmt = BoardStatements.addUserToBoard(idUser, idBoard)

        dataSource.connection.use {
            it.autoCommit = false

            it.prepareStatement(insertStmt).executeUpdate()

            it.autoCommit = true
        }
    }

    override fun searchUserBoards(idUser: Int): List<Int> {
        val dataSource = setup()
        val selectStmt = UserBoardStatements.getBoardIdsFromUser(idUser)
        val boardIds = mutableListOf<Int>()

        dataSource.connection.use {
            it.autoCommit = false

            val res = it.prepareStatement(selectStmt).executeQuery()

            while (res.next()) {
                boardIds.add(res.getInt("idBoard"))
            }

            it.autoCommit = true
        }
        return boardIds
    }

    override fun checkUserInBoard(idUser: Int, idBoard: Int) {
        val dataSource = setup()
        val selectStmt = UserBoardStatements.checkUserInBoard(idUser, idBoard)

        dataSource.connection.use {
            it.autoCommit = false

            val res = it.prepareStatement(selectStmt).executeQuery()
            res.next()

            if (res.row == 0) throw TrelloException.NotFound("Board")

            it.autoCommit = true
        }
    }

    override fun getIdUsersFromBoard(idBoard: Int): List<Int> {
        val dataSource = setup()
        val selectStmt = UserBoardStatements.getIdUsersFromBoard(idBoard)
        val userIds = mutableListOf<Int>()

        dataSource.connection.use {
            it.autoCommit = false

            val res = it.prepareStatement(selectStmt).executeQuery()

            while (res.next()) {
                userIds.add(res.getInt("idUser"))
            }

            it.autoCommit = true
        }
        return userIds
    }

    override fun getBoardCountFromUser(idUser: Int): Int {
        val dataSource = setup()
        val selectStmt = UserBoardStatements.getBoardCountFromUser(idUser)
        var count: Int

        dataSource.connection.use {
            it.autoCommit = false

            val res = it.prepareStatement(selectStmt).executeQuery()
            res.next()

            count = res.getInt("count")

            it.autoCommit = true
        }
        return count
    }

    override fun getUserCountFromBoard(idBoard: Int): Int {
        val dataSource = setup()
        val selectStmt = UserBoardStatements.getUserCountFromBoard(idBoard)
        var count: Int

        dataSource.connection.use {
            it.autoCommit = false

            val res = it.prepareStatement(selectStmt).executeQuery()
            res.next()

            count = res.getInt("count")

            it.autoCommit = false
        }
        return count
    }
}
