package pt.isel.ls.server.data.dataPostGres.dataSQL

import pt.isel.ls.server.data.dataInterfaces.ListData
import pt.isel.ls.server.data.dataPostGres.statements.ListStatement
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.BoardList
import pt.isel.ls.server.utils.setup
import java.sql.Statement

class ListDataSQL : ListData {
    override val size: Int get() = getSizeCount("idList", "list")

    override fun createList(idBoard: Int, name: String): Int {
        val dataSource = setup()
        val insertStmt = ListStatement.createListCMD(idBoard, name)
        var idList = -1

        dataSource.connection.use {
            it.autoCommit = true

            val res = it.prepareStatement(insertStmt, Statement.RETURN_GENERATED_KEYS)
            res.executeUpdate()

            if (res.generatedKeys.next()) idList = res.generatedKeys.getInt(1)

            it.autoCommit = true
        }
        return idList
    }

    override fun getList(idList: Int, idBoard: Int): BoardList {
        val dataSource = setup()
        val selectStmt = ListStatement.getListCMD(idList, idBoard)
        lateinit var list: BoardList

        dataSource.connection.use {
            it.autoCommit = false

            val res = it.prepareStatement(selectStmt).executeQuery()
            res.next()

            if (res.row == 0) throw TrelloException.NotFound("List")

            list = BoardList(
                res.getInt("idList"),
                res.getInt("idBoard"),
                res.getString("name")
            )

            it.autoCommit = true
        }
        return list
    }

    override fun getListsOfBoard(idBoard: Int, limit: Int, skip: Int): List<BoardList> {
        val dataSource = setup()
        val selectStmt = ListStatement.getListOfBoard(idBoard, limit, skip)
        val lists = mutableListOf<BoardList>()

        dataSource.connection.use {
            it.autoCommit = false

            val res = it.prepareStatement(selectStmt).executeQuery()

            while (res.next()) {
                if (res.row == 0) return emptyList() /** Estamos a dar return antes de acabar a ligação!!! **/
                lists.add(
                    BoardList(
                        res.getInt("idList"),
                        res.getInt("idBoard"),
                        res.getString("name")
                    )
                )
            }

            it.autoCommit = true
        }
        return lists
    }

    override fun deleteList(idList: Int, idBoard: Int) {
        val dataSource = setup()
        val deleteStmt = ListStatement.deleteList(idList, idBoard)

        dataSource.connection.use {
            it.autoCommit = false

            val res = it.prepareStatement(deleteStmt).executeQuery()
            res.next()

            if (res.row == 0) throw TrelloException.NoContent("List")

            it.autoCommit = true
        }
    }

    override fun getListCount(idBoard: Int): Int {
        val dataSource = setup()
        val selectStmt = ListStatement.getListCount(idBoard)
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
