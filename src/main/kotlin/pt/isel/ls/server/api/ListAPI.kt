package pt.isel.ls.server.api

import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.OK
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.http4k.routing.path
import pt.isel.ls.server.annotations.Auth
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.services.ListServices
import pt.isel.ls.server.utils.BoardListIn

class ListAPI(private val services: ListServices) {

    fun createList(request: Request): Response {
        return handleRequest(request, ::createListInternal)
    }

    fun getList(request: Request): Response {
        return handleRequest(request, ::getListInternal)
    }

    fun getListsFromBoard(request: Request): Response {
        return handleRequest(request, ::getListsFromBoardInternal)
    }

    @Auth
    private fun createListInternal(request: Request, token: String): Response {
        val idBoard = request.path("idBoard")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idBoard")
        val newList = Json.decodeFromString<BoardListIn>(request.bodyString())
        return createRsp(CREATED, services.createList(token, idBoard, newList.name))
    }

    @Auth
    private fun getListInternal(request: Request, token: String): Response {
        val idBoard = request.path("idBoard")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idBoard")
        val idList = request.path("idList")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idList")
        return createRsp(OK, services.getList(token, idBoard, idList))
    }

    @Auth
    private fun getListsFromBoardInternal(request: Request, token: String): Response {
        val idBoard = request.path("idBoard")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idBoard")
        return createRsp(OK, services.getListsOfBoard(token, idBoard))
    }

}