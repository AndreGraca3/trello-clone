package pt.isel.ls.server.utils

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
