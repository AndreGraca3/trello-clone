package pt.isel.ls.server.exceptions

import org.http4k.core.Status
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.CONFLICT
import org.http4k.core.Status.Companion.INTERNAL_SERVER_ERROR
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.NO_CONTENT
import org.http4k.core.Status.Companion.UNAUTHORIZED

sealed class TrelloException(message: String, val status: Status) : Exception(message) {
    class NotAuthorized : TrelloException("Unauthorized Operation.", UNAUTHORIZED)

    class NotFound(msg: String) : TrelloException(msg, NOT_FOUND)

    class IllegalArgument(msg: String) : TrelloException(msg, BAD_REQUEST)

    class AlreadyExists(msg: String) : TrelloException(msg, CONFLICT)

    class NoContent() : TrelloException("", NO_CONTENT)

    class InternalError() : TrelloException("Server Error", INTERNAL_SERVER_ERROR)
}

val map: Map<String, (String) -> TrelloException> = mapOf(
    "23505" to { str -> TrelloException.AlreadyExists(str) },
    "22001" to { str -> TrelloException.IllegalArgument(str) }
)

const val NOT_FOUND = "not found."

const val ALREADY_EXISTS = "already exists."

const val INVAL_PARAM = "Invalid parameter:"