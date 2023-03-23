package pt.isel.ls.server.web.api

import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import pt.isel.ls.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.http4k.core.Status
import org.http4k.routing.path
import pt.isel.ls.server.annotations.Auth
import pt.isel.ls.server.createRsp
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.services.ListServices
import pt.isel.ls.server.services.UserServices

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
        val name = request.path("name").toString()
        return createRsp(Status.CREATED, services.createList(token, idBoard, name))
    }

    @Auth
    private fun getListInternal(request: Request, token: String): Response {
        val idList = request.path("idList")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idList")
        return createRsp(OK, services.getList(token, idList))
    }

    @Auth
    private fun getListsFromBoardInternal(request: Request, token: String): Response { // No auth? Only for getBoard?
        val idBoard = request.path("idBoard")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idBoard")
        return createRsp(OK, services.getListsOfBoard(token, idBoard))
    }

}