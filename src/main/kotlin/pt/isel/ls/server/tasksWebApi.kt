package pt.isel.ls.server

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Status.Companion.UNAUTHORIZED
import org.http4k.routing.path
import org.slf4j.LoggerFactory
import pt.isel.ls.Board
import pt.isel.ls.BoardIn
import pt.isel.ls.UserIn
import pt.isel.ls.UserOut

val logger = LoggerFactory.getLogger("pt.isel.ls.http.HTTPServer")

class WebApi(private val services: Services) {

    fun postUser(request: Request): Response {
        logRequest(request)
        return try {
            val newUser = Json.decodeFromString<UserIn>(request.bodyString()) // deserializes
            val createdUser = services.createUser(newUser.name, newUser.email)
            createRsp(OK, UserOut(createdUser.first, createdUser.second))
        } catch(e: Exception) {
            createRsp(BAD_REQUEST, e.message)
        }
    }

    fun getUserDetails(request: Request): Response {
        logRequest(request)
        return try {
            val userId = request.path("idUser")?.toIntOrNull()
            if (userId != null) createRsp(OK, services.getUserInfo(userId))
            else createRsp(BAD_REQUEST, "Invalid parameters!")
        } catch(e: Exception) {
            createRsp(NOT_FOUND, e.message)
        }
    }

    fun createBoard(request: Request): Response {
        TODO()
    /*    logRequest(request)
        return try {
            val token = request.header("token")
            if(token == null) createRsp(UNAUTHORIZED, "Invalid Token!")
            val newBoard = Json.decodeFromString<BoardIn>(request.bodyString())
            val idUser = services.getUserByToken()
            createRsp(CREATED, services.createBoard(idUser, newBoard.name, newBoard.description))
        } catch(e: Exception) {
            createRsp(NOT_FOUND, e.message)
        }*/
    }
}

//Aux Functions
private inline fun <reified T> createRsp(status: Status, body: T): Response {
    return Response(status)
        .header("content-type", "application/json")
        .body(Json.encodeToString(body))
}

fun logRequest(request: Request) {
    logger.info(
        "incoming request: method={}, uri={}, content-type={} accept={}",
        request.method,
        request.uri,
        request.header("content-type"),
        request.header("accept")
    )
}