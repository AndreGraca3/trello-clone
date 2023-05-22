package pt.isel.ls.server.data.dataPostGres.dataSQL.models

import pt.isel.ls.server.data.transactionManager.transaction.ITransactionContext
import pt.isel.ls.server.data.transactionManager.transaction.SQLTransaction
import pt.isel.ls.server.data.dataInterfaces.models.UserBoardData
import pt.isel.ls.server.data.dataPostGres.statements.UserBoardStatements
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.setup

class UserBoardDataSQL : UserBoardData {

    override fun addUserToBoard(idUser: Int, idBoard: Int, ctx: ITransactionContext) {
        val insertStmt = UserBoardStatements.addUserToBoard(idUser, idBoard)
        (ctx as SQLTransaction).con.prepareStatement(insertStmt).executeUpdate()
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

    override fun checkUserInBoard(idUser: Int, idBoard: Int, ctx: ITransactionContext) {
        val selectStmt = UserBoardStatements.checkUserInBoard(idUser, idBoard)

        val res = (ctx as SQLTransaction).con.prepareStatement(selectStmt).executeQuery()
        res.next()

        if (res.row == 0) throw TrelloException.NotFound("Board")
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

    override fun getBoardCountFromUser(idUser: Int, name: String, numLists: Int?): Int {
        val dataSource = setup()
        val selectStmt = UserBoardStatements.getBoardCountFromUser(idUser, name, numLists)
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

            it.autoCommit = true
        }
        return count
    }
}
