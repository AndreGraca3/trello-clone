package pt.isel.ls.server.exceptions

import org.http4k.core.Status

sealed class WebApiException(message: String, status: Status) : TrelloException(message, status) {

}

