package pt.isel.ls.server

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Status.Companion.UNAUTHORIZED
import org.http4k.routing.path
import org.slf4j.LoggerFactory
import pt.isel.ls.*
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.exceptions.WebApiException

val logger = LoggerFactory.getLogger("pt.isel.ls.http.HTTPServer")

class WebApi(private val services: Services) {

    fun postUser(request: Request): Response {
        return handleRequest(request, ::postUserInternal)
    }

    fun getUserDetails(request: Request): Response {
        return handleRequest(request, ::getUserDetailsInternal)
    }

    fun createBoard(request: Request): Response {
        return handleRequest(request, ::createBoardInternal)
    }


    private fun postUserInternal(request: Request): Response {
        val newUser = Json.decodeFromString<UserIn>(request.bodyString()) // deserializes
        val createdUser = services.createUser(newUser.name, newUser.email)
        return createRsp(OK, UserOut(createdUser.first, createdUser.second))
    }

    private fun getUserDetailsInternal(request: Request): Response {
        val userId = request.path("idUser")?.toIntOrNull()
        return if (userId != null) createRsp(OK, services.getUserInfo(userId))
        else createRsp(BAD_REQUEST, "Invalid parameters!")
    }

    private fun createBoardInternal(request: Request): Response {
            val authHeader = request.header("Authorization") ?: return createRsp(UNAUTHORIZED, "Invalid Token!")
            val token = authHeader.removePrefix("Bearer ")
            val newBoard = Json.decodeFromString<BoardIn>(request.bodyString())
            val idUser = services.getIdUserByToken(token)
            return createRsp(CREATED, BoardOut(services.createBoard(idUser, newBoard.name, newBoard.description)))
    }

    fun getBoardInfo(request: Request): Response {
        logRequest(request)
        return try {
            val idBoard = request.path("idBoard")?.toIntOrNull()
            if (idBoard != null) createRsp(OK, services.getBoardInfo(idBoard))
            else createRsp(BAD_REQUEST, "Invalid parameters!")
        } catch (e: Exception) {
            createRsp(NOT_FOUND, e.message)
        }
    }

    fun addUserToBoard(request: Request): Response {
        logRequest(request)
        return try {
            val idBoard = request.path("idBoard")?.toIntOrNull()
            val idUser = request.path("idUser")?.toIntOrNull()
            if (idBoard != null && idUser != null) {
                services.addUserToBoard(idUser, idBoard)
                createRsp(OK, "Success!")
            } else createRsp(BAD_REQUEST, "Invalid parameters!")
        } catch (e: Exception) {
            createRsp(NOT_FOUND, e.message)
        }
    }

    fun getBoardsFromUser(request: Request) : Response {
        logRequest(request)
        return try {
            val idUser = request.path("idUser")?.toIntOrNull()
            if (idUser != null) {
                val list = services.getBoardsFromUser(idUser)
                if(list.isEmpty()) createRsp(OK, "Empty")
                createRsp(OK, list)
            } else createRsp(BAD_REQUEST, "Invalid parameters")
        } catch (e: Exception) {
            createRsp(NOT_FOUND, e.message)
        }
    }
}

//Aux Functions

private fun handleRequest(request: Request, handler: (Request) -> Response):Response {
    logRequest(request)
    return try {
        handler(request)
    } catch (e: TrelloException) {
        createRsp(e.status, e.message)
    }
}

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