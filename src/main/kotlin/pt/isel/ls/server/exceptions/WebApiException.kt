package pt.isel.ls.server.exceptions

import java.lang.Exception

sealed class WebApiException(message: String) : Exception(message) {

}

