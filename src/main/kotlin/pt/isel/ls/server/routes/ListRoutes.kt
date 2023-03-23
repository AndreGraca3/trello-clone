package pt.isel.ls.server.routes

import org.http4k.core.Method
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import pt.isel.ls.server.web.api.ListAPI
import pt.isel.ls.server.web.api.UserAPI

class ListRoutes(private val webApi: ListAPI) {
    operator fun invoke(): RoutingHttpHandler {
        return routes(
            "board/{idBoard}/list" bind Method.POST to webApi::createList,
            "board/{idBoard}/list" bind Method.GET to webApi::getListsFromBoard,
            "board/{idBoard}/list/{idList}" bind Method.GET to webApi::getList /** requer idBody? **/
        )
    }
}