package pt.isel.ls.server.web.api

import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.slf4j.LoggerFactory
import pt.isel.ls.*
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.annotations.Auth
import pt.isel.ls.server.createRsp
import pt.isel.ls.server.services.Services

val logger = LoggerFactory.getLogger("pt.isel.ls.http.HTTPServer")

class TasksApi(private val services: Services) {    //perhaps should this be sealed or open class??
    val userAPI = UserAPI(services.userServices)
    val boardAPI = BoardAPI(services.boardServices)
    val listAPI = ListAPI(services.listServices)
    val cardAPI = CardAPI(services.cardServices)
}


// Aux Functions
private fun getToken(request: Request): String {
    val authHeader = request.header("Authorization")
    return authHeader?.removePrefix("Bearer ") ?: throw TrelloException.NotAuthorized()
}

fun handleRequest(request: Request, handler: (Request, String) -> Response): Response {
    logRequest(request)
    return try {
        if (TODO())
            handler(request, getToken(request))
        else
            handler(request, "null")
    } catch (e: Exception) {
        if (e is TrelloException) {
            createRsp(e.status, e.message)
        } else {
            createRsp(BAD_REQUEST, e.message)
        }
    }
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
