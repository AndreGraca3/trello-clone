package pt.isel.ls.server.data.dataPostGres.dataSQL.models

import pt.isel.ls.server.UserProfile
import pt.isel.ls.server.data.dataInterfaces.models.UserBoardData
import pt.isel.ls.server.data.dataPostGres.statements.UserBoardStatements
import pt.isel.ls.server.data.dataPostGres.statements.UserStatements
import pt.isel.ls.server.data.transactionManager.transactions.TransactionCtx
import pt.isel.ls.server.exceptions.NOT_FOUND
import pt.isel.ls.server.exceptions.TrelloException

class UserBoardDataSQL : UserBoardData {

    override fun addUserToBoard(idUser: Int, idBoard: Int, ctx: TransactionCtx) : UserProfile{
        val insertStmt = UserBoardStatements.addUserToBoard(idUser, idBoard)
        val selectStmt = UserStatements.getUserProfile(idUser)
        ctx.con.prepareStatement(insertStmt).executeUpdate()
        val res = ctx.con.prepareStatement(selectStmt).executeQuery()
        res.next()
        return UserProfile(res.getString("avatar"), res.getString("name"))
    }

    override fun searchUserBoards(idUser: Int, ctx: TransactionCtx): List<Int> {
        val selectStmt = UserBoardStatements.getBoardIdsFromUser(idUser)
        val boardIds = mutableListOf<Int>()

        val res = ctx.con.prepareStatement(selectStmt).executeQuery()

        while (res.next()) {
            boardIds.add(res.getInt("idBoard"))
        }

        return boardIds
    }

    override fun checkUserInBoard(idUser: Int, idBoard: Int, ctx: TransactionCtx) {
        val selectStmt = UserBoardStatements.checkUserInBoard(idUser, idBoard)

        val res = ctx.con.prepareStatement(selectStmt).executeQuery()
        res.next()

        if (res.row == 0) throw TrelloException.NotFound("Board $NOT_FOUND")
    }

    override fun getIdUsersFromBoard(idBoard: Int, ctx: TransactionCtx): List<Int> {
        val selectStmt = UserBoardStatements.getIdUsersFromBoard(idBoard)
        val userIds = mutableListOf<Int>()

        val res = ctx.con.prepareStatement(selectStmt).executeQuery()

        while (res.next()) {
            userIds.add(res.getInt("idUser"))
        }

        return userIds
    }

    override fun getBoardCountFromUser(idUser: Int, name: String, numLists: Int?, ctx: TransactionCtx): Int {
        val selectStmt = UserBoardStatements.getBoardCountFromUser(idUser, name, numLists)

        val res = ctx.con.prepareStatement(selectStmt).executeQuery()
        res.next()

        return res.getInt("count")
    }

    override fun getUserCountFromBoard(idBoard: Int, ctx: TransactionCtx): Int {
        val selectStmt = UserBoardStatements.getUserCountFromBoard(idBoard)

        val res = ctx.con.prepareStatement(selectStmt).executeQuery()
        res.next()

        return res.getInt("count")
    }
}
