package pt.isel.ls.server.data.dataPostGres.dataSQL.models

import pt.isel.ls.server.data.transactionManager.transactions.TransactionCtx
import pt.isel.ls.server.data.transactionManager.transactions.SQLTransaction
import pt.isel.ls.server.data.dataInterfaces.models.ListData
import pt.isel.ls.server.data.dataPostGres.statements.ListStatement
import pt.isel.ls.server.exceptions.NOT_FOUND
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.BoardList

class ListDataSQL : ListData {

    override fun createList(idBoard: Int, name: String, ctx: TransactionCtx): Int {
        val insertStmt = ListStatement.createListCMD(idBoard, name)

        val res = ctx.con.prepareStatement(insertStmt).executeQuery()
        res.next()

        return res.getInt("idList")
    }

    override fun getList(idList: Int, idBoard: Int, ctx: TransactionCtx): BoardList {
        val selectStmt = ListStatement.getListCMD(idList, idBoard)
        lateinit var list: BoardList

        val res = ctx.con.prepareStatement(selectStmt).executeQuery()
        res.next()

        if (res.row == 0) throw TrelloException.NotFound("List $NOT_FOUND")

        list = BoardList(
            res.getInt("idList"),
            res.getInt("idBoard"),
            res.getString("name")
        )

        return list
    }

    override fun getListsOfBoard(idBoard: Int, ctx: TransactionCtx): List<BoardList> {
        val selectStmt = ListStatement.getListsOfBoard(idBoard)
        val lists = mutableListOf<BoardList>()

        val res = ctx.con.prepareStatement(selectStmt).executeQuery()

        while (res.next()) {
            if (res.row == 0) return emptyList()
            lists.add(
                BoardList(
                    res.getInt("idList"),
                    res.getInt("idBoard"),
                    res.getString("name")
                )
            )
        }
        return lists
    }

    override fun deleteList(idList: Int, idBoard: Int, ctx: TransactionCtx) {
        val deleteStmt = ListStatement.deleteList(idList, idBoard)
        val res = ctx.con.prepareStatement(deleteStmt).executeQuery()
        res.next()
        if(res.row == 0) throw TrelloException.NoContent()
    }

    override fun getListCount(idBoard: Int, ctx: TransactionCtx): Int {
        val selectStmt = ListStatement.getListCount(idBoard)

        val res = ctx.con.prepareStatement(selectStmt).executeQuery()
        res.next()

        return res.getInt("count")
    }
}
