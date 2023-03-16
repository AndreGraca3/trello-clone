package pt.isel.ls.server

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.ACCEPTED
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Status.Companion.UNAUTHORIZED
import org.http4k.routing.path
import org.slf4j.LoggerFactory
import pt.isel.ls.*
import pt.isel.ls.server.exceptions.TrelloException

val logger = LoggerFactory.getLogger("pt.isel.ls.http.HTTPServer")

class WebApi(private val services: Services) {

    fun postUser(request: Request): Response {
        return handleRequest(request, ::postUserInternal)
    }

    fun getUserInfo(request: Request): Response {
        return handleRequest(request, ::getUserDetailsInternal)
    }

    fun createBoard(request: Request): Response {
        return handleRequest(request, ::createBoardInternal)
    }

    fun addUserToBoard(request: Request): Response {
        return handleRequest(request, ::addUserToBoardInternal)
    }

    fun getBoardInfo(request: Request): Response {
        return handleRequest(request, ::getBoardInfoInternal)
    }

    fun getBoardsFromUser(request: Request): Response {
        return handleRequest(request, ::getBoardsFromUserInternal) //check if this return boards or idBoard's
    }

    fun createNewListInBoard(request: Request): Response {
        return handleRequest(request, ::createNewListInBoardInternal)
    }

    fun getListInfo(request: Request) : Response {
        return handleRequest(request, ::getListInfoInternal)
    }

    fun getListFromBoard(request: Request) : Response {
        return handleRequest(request, ::getListFromBoardInternal)
    }

    /** internal functions , logic behind the scenes **/

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

    private fun createBoardInternal(request: Request): Response { //auth needed
        val token = checkIfAuthorized(request)
        val newBoard = Json.decodeFromString<BoardIn>(request.bodyString()) // deserializes
        return if(token != null) {
            createRsp(CREATED, BoardOut(services.createBoard(token, newBoard.name, newBoard.description)))
        } else createRsp(UNAUTHORIZED,"Invalid Token!")
    }

    private fun getBoardInfoInternal(request: Request): Response { // auth needed //TODO
        val token = checkIfAuthorized(request) // from user logged
        val idBoard = request.path("idBoard")?.toIntOrNull()
        return if (idBoard != null && token != null){
            val idUser = services.getIdUserByToken(token)

            createRsp(OK, services.getBoardInfo(idBoard, idUser))
        }
        else createRsp(BAD_REQUEST, "Invalid parameters!")
    }

    private fun addUserToBoardInternal(request: Request): Response { //auth needed
        val token = checkIfAuthorized(request) // from user logged
        val idBoard = request.path("idBoard")?.toIntOrNull()
        val idUser = request.path("idUser")?.toIntOrNull()
        return if (idBoard != null && idUser != null && token != null) {
            if(idUser != services.getIdUserByToken(token)) createRsp(UNAUTHORIZED, "Invalid Token")
            services.addUserToBoard(token,idUser, idBoard)
            createRsp(ACCEPTED, "Success!")
        } else createRsp(BAD_REQUEST, "Invalid parameters!")
    }

    private fun getBoardsFromUserInternal(request: Request): Response {
        val idUser = request.path("idUser")?.toIntOrNull()
        return if (idUser != null) {
            val list = services.getBoardsFromUser(idUser)
            if (list.isEmpty()) createRsp(OK, "Empty")
            createRsp(OK, list)
        } else createRsp(BAD_REQUEST, "Invalid parameters")
    }

    private fun createNewListInBoardInternal(request: Request): Response { //auth needed
        val token = checkIfAuthorized(request)
        val idBoard = request.path("idBoard")?.toIntOrNull() // Note to self : isto dá badRequest por causa do try catch
        val name = request.path("name").toString()
        return if(token != null) {
            val idList = services.createNewListInBoard(idBoard!!,name) // sendo que se o idBoard não existir isto dá exception consigo garantir que não chega aqui a Null.
            createRsp(CREATED,idList)
        } else {
            createRsp(UNAUTHORIZED,"Invalid Token!")
        }
    }

    private fun getListInfoInternal(request: Request): Response {
        val token = checkIfAuthorized(request)
        val idBoard = request.path("idBoard")?.toIntOrNull()
        val idList = request.path("idList")?.toIntOrNull()
        return if(token != null) {
            val list = services.getListInfo(idBoard!!,idList!!)
            createRsp(OK,list)
        } else {
            createRsp(UNAUTHORIZED,"Invalid Token!")
        }
    }

    private fun getListFromBoardInternal(request: Request): Response {
        val token = checkIfAuthorized(request) // Nota : estou farto de escrever isto XD
        /** tenho de verificar se o user pertence a este board. **/
        val idBoard = request.path("idBoard")?.toIntOrNull()
        return if(token != null) {
            val lists = services.getListsOfBoard(idBoard!!)
            createRsp(OK,lists)
        } else {
            createRsp(UNAUTHORIZED,"Invalid Token!")
        }
    }

}


//Aux Functions

private fun checkIfAuthorized(request: Request) : String? { /** devia retornar uma response, para não verificar repetidamente. **/
    val authHeader = request.header("Authorization")
    return authHeader?.removePrefix("Bearer ")
}

private fun handleRequest(request: Request, handler: (Request) -> Response): Response {
    logRequest(request)
    return try {
        handler(request)
    } catch (e: Exception) { /** perguntar ao martin **/
        if (e is TrelloException)
            createRsp(e.status, e.message)
        else createRsp(BAD_REQUEST, e.message)
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