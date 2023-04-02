package pt.isel.ls.server.utils

import org.postgresql.ds.PGSimpleDataSource
import org.slf4j.LoggerFactory
import pt.isel.ls.server.exceptions.TrelloException
import java.time.LocalDate

val logger = LoggerFactory.getLogger("pt.isel.ls.http.HTTPServer")

fun checkEndDate(endDate: String) {
    val endDateParsed = LocalDate.parse(endDate) // 2023-03-14
    if (endDateParsed < LocalDate.now()) throw TrelloException.IllegalArgument(endDate)
}

fun isValidString(value: String): Boolean {
    val trim = value.trim()
    if (trim != "" && trim != "null") return true
    throw TrelloException.IllegalArgument(value)
}

fun setup(): PGSimpleDataSource {
    val dataSource = PGSimpleDataSource()
    val jdbcDatabaseURL = System.getenv("JDBC_DATABASE_URL")
    dataSource.setURL(jdbcDatabaseURL)
    return dataSource
}
