package pt.isel.ls.server.api

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.OK
import pt.isel.ls.server.BoardListIn
import pt.isel.ls.server.annotations.Auth
import pt.isel.ls.server.services.ListServices

class ListAPI(private val services: ListServices) {

    @Auth
    fun createList(request: Request, token: String): Response {
        val idBoard = getPathParam(request, "idBoard")
        val newList = Json.decodeFromString<BoardListIn>(request.bodyString())
        return createRsp(CREATED, services.createList(token, idBoard, newList.name))
    }

    @Auth
    fun getList(request: Request, token: String): Response {
        val idBoard = getPathParam(request, "idBoard")
        val idList = getPathParam(request, "idList")
        return createRsp(OK, services.getList(token, idBoard, idList))
    }

    @Auth
    fun getCardsFromList(request: Request, token: String): Response {
        val idBoard = getPathParam(request, "idBoard")
        val idList = getPathParam(request, "idList")
        val limit = getQueryParam(request, "limit")?.toIntOrNull()
        val skip = getQueryParam(request, "skip")?.toIntOrNull()
        return createRsp(OK, services.getCardsFromList(token, idBoard, idList, limit, skip))
    }

    @Auth
    fun getListsFromBoard(request: Request, token: String): Response {
        val idBoard = getPathParam(request, "idBoard")
        return createRsp(OK, services.getListsOfBoard(token, idBoard))
    }

    @Auth
    fun deleteList(request: Request, token: String): Response {
        val idBoard = getPathParam(request, "idBoard")
        val idList = getPathParam(request, "idList")
        val action = getQueryParam(request, "action")
        return createRsp(OK, services.deleteList(token, idBoard, idList, action))
    }
}
