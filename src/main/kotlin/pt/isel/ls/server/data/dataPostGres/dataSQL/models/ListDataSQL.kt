package pt.isel.ls.server.data.dataPostGres.dataSQL.models

import pt.isel.ls.server.data.dataInterfaces.models.ListData
import pt.isel.ls.server.data.dataPostGres.statements.ListStatement
import pt.isel.ls.server.exceptions.NOT_FOUND
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.BoardList
import java.sql.Connection

class ListDataSQL : ListData {

    override fun createList(idBoard: Int, name: String, con: Connection): Int {
        val insertStmt = ListStatement.createListCMD(idBoard, name)

        val res = con.prepareStatement(insertStmt).executeQuery()
        res.next()

        return res.getInt("idList")
    }

    override fun getList(idList: Int, idBoard: Int, con: Connection): BoardList {
        val selectStmt = ListStatement.getListCMD(idList, idBoard)
        lateinit var list: BoardList

        val res = con.prepareStatement(selectStmt).executeQuery()
        res.next()

        if (res.row == 0) throw TrelloException.NotFound("List $NOT_FOUND")

        list = BoardList(
            res.getInt("idList"),
            res.getInt("idBoard"),
            res.getString("name")
        )

        return list
    }

    override fun getListsOfBoard(idBoard: Int, con: Connection): List<BoardList> {
        val selectStmt = ListStatement.getListsOfBoard(idBoard)
        val lists = mutableListOf<BoardList>()

        val res = con.prepareStatement(selectStmt).executeQuery()

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

    override fun deleteList(idList: Int, idBoard: Int, con: Connection) {
        val deleteStmt = ListStatement.deleteList(idList, idBoard)
        con.prepareStatement(deleteStmt).executeUpdate()
    }

    override fun getListCount(idBoard: Int, con: Connection): Int {
        val selectStmt = ListStatement.getListCount(idBoard)

        val res = con.prepareStatement(selectStmt).executeQuery()
        res.next()

        return res.getInt("count")
    }
}
