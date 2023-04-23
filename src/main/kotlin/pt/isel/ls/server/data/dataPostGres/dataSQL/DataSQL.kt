package pt.isel.ls.server.data.dataPostGres.dataSQL

import pt.isel.ls.server.data.dataInterfaces.Data
import pt.isel.ls.server.data.dataPostGres.statements.BoardStatements
import pt.isel.ls.server.utils.setup

class DataSQL : Data {
    override val userData = UserDataSQL()
    override val boardData = BoardDataSQL()
    override val userBoardData = UserBoardDataSQL()
    override val listData = ListDataSQL()
    override val cardData = CardDataSQL()
}

fun getSize(id: String, table: String): Int {
    val dataSource = setup()
    val selectStmt = size(id,table)
    var res: Int

    dataSource.connection.use {
        it.autoCommit = false

        val stmt = it.prepareStatement(selectStmt).executeQuery()
        stmt.next()

        res = stmt.getInt("count")
        it.autoCommit = true
    }
    return res
}

fun size(id: String, table: String): String {
    return "SELECT COUNT($id) FROM dbo.$table;"
}
