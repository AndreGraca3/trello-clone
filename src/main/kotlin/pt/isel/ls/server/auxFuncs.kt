package pt.isel.ls.server

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Response
import org.http4k.core.Status
import pt.isel.ls.Board
import pt.isel.ls.BoardList
import pt.isel.ls.Card
import pt.isel.ls.User
import pt.isel.ls.server.exceptions.TrelloException
import java.time.LocalDate

// Auxiliary functions
/*fun initialState() { // for test purposes
    users.clear()
    boards.clear()
    lists.clear()
    cards.clear()
}*/

fun isValidString(value: String): Boolean {
    val trim = value.trim()
    if (trim != "" && trim != "null") return true
    throw TrelloException.IllegalArgument(value)
}

fun checkEndDate(endDate: String) {
    val endDateParsed = LocalDate.parse(endDate) // 2023-03-14
    if (endDateParsed < LocalDate.now()) throw TrelloException.IllegalArgument(endDate)
}

fun getNextId(list: List<*>): Int {
    return when (list.firstOrNull()) {
        is User -> (list as List<User>).last().idUser.inc()
        is Board -> (list as List<Board>).last().idBoard.inc()
        is BoardList -> (list as List<BoardList>).last().idList.inc()
        is Card -> (list as List<Card>).last().idCard.inc()
        else -> 0
    }
}

inline fun <reified T> createRsp(status: Status, body: T): Response {
    return Response(status)
        .header("content-type", "application/json")
        .body(Json.encodeToString(body))
}