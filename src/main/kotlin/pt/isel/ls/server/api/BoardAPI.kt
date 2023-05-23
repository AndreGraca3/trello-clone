package pt.isel.ls.server.api

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.OK
import pt.isel.ls.server.annotations.Auth
import pt.isel.ls.server.services.BoardServices
import pt.isel.ls.server.utils.BoardIn
import pt.isel.ls.server.utils.BoardOut
import pt.isel.ls.server.utils.IDUser

class BoardAPI(private val services: BoardServices) {

    @Auth
    fun createBoard(request: Request, token: String): Response {
        val newBoard = Json.decodeFromString<BoardIn>(request.bodyString())
        return createRsp(CREATED, BoardOut(services.createBoard(token, newBoard.name, newBoard.description)))
    }

    @Auth
    fun getBoard(request: Request, token: String): Response {
        val idBoard = getPathParam(request, "idBoard")
        return createRsp(OK, services.getBoard(token, idBoard))
    }

    @Auth
    fun addUserToBoard(request: Request, token: String): Response {
        val idBoard = getPathParam(request, "idBoard")
        val objIdUser = Json.decodeFromString<IDUser>(request.bodyString())
        return createRsp(OK, services.addUserToBoard(token, objIdUser.idUser, idBoard))
    }

    @Auth
    fun getBoardsFromUser(request: Request, token: String): Response {
        val limit = getQueryParam(request, "limit")?.toIntOrNull()
        val skip = getQueryParam(request, "skip")?.toIntOrNull()
        val name = getQueryParam(request, "name")
        val numLists = getQueryParam(request, "numLists")?.toIntOrNull()
        return createRsp(OK, services.getBoardsFromUser(token, limit, skip, name, numLists))
    }

    @Auth
    fun getUsersFromBoard(request: Request, token: String): Response {
        val idBoard = getPathParam(request, "idBoard")
        return createRsp(OK, services.getUsersFromBoard(token, idBoard))
    }
}
