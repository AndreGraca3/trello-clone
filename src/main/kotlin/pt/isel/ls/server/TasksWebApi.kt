package pt.isel.ls.server

import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.path
import org.slf4j.LoggerFactory
import pt.isel.ls.server.exceptions.TrelloException

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import pt.isel.ls.*

val logger = LoggerFactory.getLogger("pt.isel.ls.http.HTTPServer")

class WebApi(private val services: Services) {

    // @NotAuthorization
    fun createUser(request: Request): Response {
        return handleRequest(request, false, ::createUserInternal)
    }

    fun getUser(request: Request): Response {
        return handleRequest(request, true, ::getUserInternal) /** tinhamos optado por necessitar login.**/
    }

    fun createBoard(request: Request): Response {
        return handleRequest(request, true, ::createBoardInternal)
    }

    fun addUserToBoard(request: Request): Response {
        return handleRequest(request, true, ::addUserToBoardInternal)
    }

    fun getBoard(request: Request): Response {
        return handleRequest(request, true, ::getBoardInternal)
    }

    fun getBoardsFromUser(request: Request): Response {
        return handleRequest(request, true, ::getBoardsFromUserInternal) // check if this return boards or idBoard's
    }

    fun createList(request: Request): Response {
        return handleRequest(request, true, ::createListInternal)
    }

    fun getList(request: Request): Response {
        return handleRequest(request, true, ::getListInternal)
    }

    fun getListsFromBoard(request: Request): Response {
        return handleRequest(request, true, ::getListsFromBoardInternal)
    }

    fun createCard(request: Request): Response {
        return handleRequest(request, true, ::createCardInternal)
    }

    fun getCard(request: Request): Response {
        return handleRequest(request, true, ::getCardInternal)
    }

    fun getCardsFromList(request: Request): Response {
        return handleRequest(request, true, ::getCardsFromListInternal)
    }

    fun moveCard(request: Request): Response {
        return handleRequest(request, true, ::moveCardInternal)
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

    private fun getUserInternal(request: Request, token: String): Response {
        val user = services.getUser(token)
        return createRsp(OK, user)
    }

    /** ----------------------------
     *  Board Management
     *  ------------------------------**/

    private fun createBoardInternal(request: Request, token: String): Response {
        val newBoard = Json.decodeFromString<BoardIn>(request.bodyString())
        return createRsp(CREATED, BoardOut(services.createBoard(token, newBoard.name, newBoard.description)))
    }

    private fun getBoardInternal(request: Request, token: String): Response {
        val idBoard = request.path("idBoard")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idBoard")
        return createRsp(OK, services.getBoard(token, idBoard))
    }

    private fun addUserToBoardInternal(request: Request, token: String): Response {
        val idBoard = request.path("idBoard")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idBoard")
        val idUser = request.path("idUser")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idUser")
        return createRsp(OK, services.addUserToBoard(token, idUser, idBoard))
    }

    private fun getBoardsFromUserInternal(request: Request, token: String): Response {
        return createRsp(OK, services.getBoardsFromUser(token)) // should return empty message?
    }

    /** ----------------------------
     *  List Management
     *  ------------------------------**/

    private fun createListInternal(request: Request, token: String): Response {
        val idBoard = request.path("idBoard")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idBoard")
        val newList = Json.decodeFromString<BoardListIn>(request.bodyString())
        return createRsp(CREATED, services.createList(token, idBoard, newList.name))
    }

    private fun getListInternal(request: Request, token: String): Response { // No auth? Only for getBoard?
        val idList = request.path("idList")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idList")
        return createRsp(OK, services.getList(token, idList))
    }

    private fun getListsFromBoardInternal(request: Request, token: String): Response { // No auth? Only for getBoard?
        val idBoard = request.path("idBoard")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idBoard")
        return createRsp(OK, services.getListsOfBoard(token, idBoard))
    }

    /** ----------------------------
     *  Card Management
     *  ------------------------------**/

    private fun createCardInternal(
        request: Request,
        token: String
    ): Response { // Why is this POST and not PUT like createList?
        val idList = request.path("idList")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idList")
        val newCard = Json.decodeFromString<CardIn>(request.bodyString())
        return createRsp(
            CREATED,
            services.createCard(token, idList, newCard.name, newCard.description, newCard.endDate)
        )
    }

    private fun getCardInternal(request: Request, token: String): Response {
        val idCard = request.path("idCard")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idCard")
        val card = services.getCard(token, idCard)
        return createRsp(OK, CardOut(card.name, card.description, card.startDate, card.endDate, card.archived))
    }

    private fun getCardsFromListInternal(request: Request, token: String): Response {
        val idList = request.path("idList")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idList")
        return createRsp(OK, services.getCardsFromList(token, idList))
    }

    private fun moveCardInternal(request: Request, token: String): Response {
        val idListDst = Json.decodeFromString<Int>(request.bodyString())
        val idCard = request.path("idCard")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idCard")
        return createRsp(OK, services.moveCard(token, idCard, idListDst))
    }
}


// Aux Functions
private fun getToken(request: Request): String {
    val authHeader = request.header("Authorization")
    return authHeader?.removePrefix("Bearer ") ?: throw TrelloException.NotAuthorized()
}

private fun handleRequest(request: Request, auth: Boolean, handler: (Request, String) -> Response): Response {
    /** Is it possible to have a function to receive and check N arguments else throw Exception?*/
    logRequest(request)
    return try {
        if (auth) {
            handler(request, getToken(request))
        } else {
            handler(request, "null")
        }
    } catch (e: Exception) {
        if (e is TrelloException) {
            createRsp(e.status, e.message)
        } else {
            createRsp(BAD_REQUEST, e.message)
        }
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
