package pt.isel.ls.server.routes

import org.http4k.core.Method
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import pt.isel.ls.server.web.api.BoardAPI
import pt.isel.ls.server.web.api.UserAPI

class BoardRoutes(private val webApi: BoardAPI) {
    operator fun invoke(): RoutingHttpHandler {
        return routes(
            "board" bind Method.POST to webApi::createBoard,
            "board" bind Method.GET to webApi::getBoardsFromUser,
            "board/{idBoard}/{idUser}" bind Method.PUT to webApi::addUserToBoard, /** verificar este path com o martin ( path ou body)**/
            "board/{idBoard}" bind Method.GET to webApi::getBoard
        )
    }
}