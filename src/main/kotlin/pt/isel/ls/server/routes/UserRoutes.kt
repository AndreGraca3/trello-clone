package pt.isel.ls.server.routes

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import pt.isel.ls.server.api.UserAPI
import pt.isel.ls.server.api.handleRequest

class UserRoutes(private val webApi: UserAPI) {
    operator fun invoke(): RoutingHttpHandler {
        return routes(
            "user" bind Method.POST to { request: Request -> handleRequest(request, webApi::createUser) },
            "user" bind Method.GET to { request: Request -> handleRequest(request, webApi::getUser) },
            "user" bind Method.PUT to { request: Request -> handleRequest(request, webApi::changeAvatar) },
            "user/login" bind Method.POST to { request: Request -> handleRequest(request, webApi::login) }
        )
    }
}
