package pt.isel.ls.server.routes

import org.http4k.core.Method
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import pt.isel.ls.server.WebApi

class BoardRoutes(private val webApi: WebApi) {
    operator fun invoke(): RoutingHttpHandler {
        return routes(
            "board" bind Method.POST to webApi::createBoard,
            "board" bind Method.GET to webApi::getBoardsFromUser,
            "board/{idBoard}" bind Method.PUT to webApi::addUserToBoard,
            "board/{idBoard}" bind Method.GET to webApi::getBoard
        )
    }
}