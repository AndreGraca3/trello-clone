package pt.isel.ls.server

import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.path
import org.slf4j.LoggerFactory
import pt.isel.ls.server.utils.BoardIn
import pt.isel.ls.server.utils.BoardListIn
import pt.isel.ls.server.utils.BoardOut
import pt.isel.ls.server.utils.CardIn
import pt.isel.ls.server.utils.CardOut
import pt.isel.ls.server.utils.UserIn
import pt.isel.ls.server.utils.UserOut
import pt.isel.ls.server.exceptions.TrelloException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import pt.isel.ls.server.annotations.Auth
import kotlin.reflect.KFunction
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible

val logger = LoggerFactory.getLogger("pt.isel.ls.http.HTTPServer")

class WebApi(private val services: Services) {

    fun createUser(request: Request): Response {
        return handleRequest(request, ::createUserInternal)
    }

    fun getUser(request: Request): Response {
        return handleRequest(request, ::getUserInternal)
    }

    fun createBoard(request: Request): Response {
        return handleRequest(request, ::createBoardInternal)
    }

    fun addUserToBoard(request: Request): Response {
        return handleRequest(request, ::addUserToBoardInternal)
    }

    fun getBoard(request: Request): Response {
        return handleRequest(request, ::getBoardInternal)
    }

    fun getBoardsFromUser(request: Request): Response {
        return handleRequest(request, ::getBoardsFromUserInternal) // check if this return boards or idBoard's
    }

    fun createList(request: Request): Response {
        return handleRequest(request, ::createListInternal)
    }

    fun getList(request: Request): Response {
        return handleRequest(request, ::getListInternal)
    }

    fun getListsFromBoard(request: Request): Response {
        return handleRequest(request, ::getListsFromBoardInternal)
    }

    fun createCard(request: Request): Response {
        return handleRequest(request, ::createCardInternal)
    }

    fun getCard(request: Request): Response {
        return handleRequest(request, ::getCardInternal)
    }

    fun getCardsFromList(request: Request): Response {
        return handleRequest(request, ::getCardsFromListInternal)
    }

    fun moveCard(request: Request): Response {
        return handleRequest(request, ::moveCardInternal)
    }

    /** internal functions , logic behind the scenes **/

    /** ----------------------------
     *  User Management
     *  ------------------------------**/

     private fun createUserInternal(request: Request, token: String): Response {
        val newUser = Json.decodeFromString<UserIn>(request.bodyString())
        val createdUser = services.createUser(newUser.name, newUser.email)
        return createRsp(CREATED, UserOut(createdUser.first, createdUser.second))
    }

    @Auth
     private fun getUserInternal(request: Request, token: String): Response {
        val user = services.getUser(token)
        return createRsp(OK, user)
    }

    /** ----------------------------
     *  Board Management
     *  ------------------------------**/

    @Auth
    private fun createBoardInternal(request: Request, token: String): Response {
        val newBoard = Json.decodeFromString<BoardIn>(request.bodyString())
        return createRsp(CREATED, BoardOut(services.createBoard(token, newBoard.name, newBoard.description)))
    }

    @Auth
    private fun getBoardInternal(request: Request, token: String): Response {
        val idBoard = request.path("idBoard")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idBoard")
        return createRsp(OK, services.getBoard(token, idBoard))
    }

    @Auth
    private fun addUserToBoardInternal(request: Request, token: String): Response {
        val idBoard = request.path("idBoard")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idBoard")
        val idUser = Json.decodeFromString<Int>(request.bodyString())
        return createRsp(OK, services.addUserToBoard(token, idUser, idBoard))
    }

    @Auth
    private fun getBoardsFromUserInternal(request: Request, token: String): Response {
        return createRsp(OK, services.getBoardsFromUser(token)) // should return empty message?
    }

    /** ----------------------------
     *  List Management
     *  ------------------------------**/

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

    /** ----------------------------
     *  Card Management
     *  ------------------------------**/

    @Auth
    private fun createCardInternal(
        request: Request,
        token: String
    ): Response { // Why is this POST and not PUT like createList?
        val idBoard = request.path("idBoard")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idBoard")
        val idList = request.path("idList")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idList")
        val newCard = Json.decodeFromString<CardIn>(request.bodyString())
        return createRsp(
            CREATED,
            services.createCard(token, idList, idBoard, newCard.name, newCard.description, newCard.endDate)
        )
    }

    @Auth
    private fun getCardInternal(request: Request, token: String): Response {
        val idBoard = request.path("idBoard")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idBoard")
        val idList = request.path("idList")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idList")
        val idCard = request.path("idCard")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idCard")
        val card = services.getCard(token, idBoard, idList, idCard)
        return createRsp(OK, CardOut(card.name, card.description, card.startDate, card.endDate, card.archived))
    }

    @Auth
    private fun getCardsFromListInternal(request: Request, token: String): Response {
        val idBoard = request.path("idBoard")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idBoard")
        val idList = request.path("idList")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idList")
        return createRsp(OK, services.getCardsFromList(token, idBoard, idList))
    }

    @Auth
    private fun moveCardInternal(request: Request, token: String): Response {
        val idBoard = request.path("idBoard")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idBoard")
        val idListNow = request.path("idList")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idList")
        val idListDst = Json.decodeFromString<Int>(request.bodyString())
        val idCard = request.path("idCard")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idCard")
        return createRsp(OK, services.moveCard(token, idCard, idBoard, idListNow, idListDst))
    }
}

// API Aux Functions
private fun getToken(request: Request): String {
    val authHeader = request.header("Authorization")
    return authHeader?.removePrefix("Bearer ") ?: throw TrelloException.NotAuthorized()
}


private fun handleRequest(request: Request, handler: KFunction<Response>): Response {
    /** We HAVE to get rid of auth param, annotation doesn't work tho*/
    logRequest(request)
    handler.isAccessible = true
    return try {
        if (isAuthRequired(handler)) {
            handler.call(request, getToken(request))
        } else {
            handler.call(request, "null")
        }
    } catch (e: Exception) {
        if (e is TrelloException) {
            createRsp(e.status, e.message)
        } else {
            createRsp(BAD_REQUEST, e.message)
        }
    }
}

private fun isAuthRequired(function: KFunction<*>): Boolean {
    return function.hasAnnotation<Auth>()
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