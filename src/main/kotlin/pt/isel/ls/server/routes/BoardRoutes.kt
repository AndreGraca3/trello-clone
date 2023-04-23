package pt.isel.ls.server.routes

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import pt.isel.ls.server.api.BoardAPI
import pt.isel.ls.server.api.handleRequest

class BoardRoutes(private val webApi: BoardAPI) {
    operator fun invoke(): RoutingHttpHandler {
        return routes(
            "board" bind Method.POST to { request: Request -> handleRequest(request, webApi::createBoard) },
            "board/{idBoard}" bind Method.GET to { request: Request -> handleRequest(request, webApi::getBoard) },
            "board" bind Method.GET to { request: Request -> handleRequest(request, webApi::getBoardsFromUser) },
            "board/{idBoard}" bind Method.PUT to { request: Request -> handleRequest(request, webApi::addUserToBoard) },
            "board/{idBoard}/allUsers" bind Method.GET to { request: Request -> handleRequest(request, webApi::getUsersFromBoard) }
        )
    }
}
