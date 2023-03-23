package pt.isel.ls.server.exceptions

import org.http4k.core.Status
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.CONFLICT
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.UNAUTHORIZED
import java.lang.Exception

sealed class TrelloException(message: String, val status: Status) : Exception(message) {
    class NotAuthorized : TrelloException("Unauthorized Operation.", UNAUTHORIZED)

    class NotFound(obj: String) : TrelloException("$obj not found.", NOT_FOUND)

    class IllegalArgument(obj: String) : TrelloException("Invalid parameters: $obj", BAD_REQUEST)

    class AlreadyExists(obj: String) : TrelloException("$obj already exists.", CONFLICT)
}
