package pt.isel.ls.server.utils

import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.server.data.dataPostGres.statements.BoardStatements
import java.time.LocalDate
import pt.isel.ls.server.exceptions.TrelloException


fun checkEndDate(endDate: String) {
    val endDateParsed = LocalDate.parse(endDate) // 2023-03-14
    if (endDateParsed < LocalDate.now()) throw TrelloException.IllegalArgument(endDate)
}

fun isValidString(value: String): Boolean {
    val trim = value.trim()
    if (trim != "" && trim != "null") return true
    throw TrelloException.IllegalArgument(value)
}

private fun setup(): PGSimpleDataSource {
    val dataSource = PGSimpleDataSource()
    val jdbcDatabaseURL = System.getenv("JDBC_DATABASE_URL")
    dataSource.setURL(jdbcDatabaseURL)
    return dataSource
}

val dataSource = setup()
val selectStmt = BoardStatements.checkUserInBoard(idUser,idBoard)

dataSource.connection.use {
    it.autoCommit = false
    val res = it.prepareStatement(selectStmt).executeQuery()
    res.next()

    if (res.row == 0) throw TrelloException.NotFound("Board")

    it.autoCommit = true
}