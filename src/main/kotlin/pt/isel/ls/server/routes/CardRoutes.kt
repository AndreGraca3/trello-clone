package pt.isel.ls.server.routes

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import pt.isel.ls.server.api.CardAPI
import pt.isel.ls.server.api.handleRequest

class CardRoutes(private val webApi: CardAPI) {
    operator fun invoke(): RoutingHttpHandler {
        return routes(
            "board/{idBoard}/card" bind Method.POST to { request: Request ->
                handleRequest(
                    request,
                    webApi::createCard
                )
            },
            "board/{idBoard}/card/{idCard}" bind Method.GET to { request: Request ->
                handleRequest(
                    request,
                    webApi::getCard
                )
            },
            "board/{idBoard}/card/{idCard}" bind Method.PUT to { request: Request ->
                handleRequest(
                    request,
                    webApi::moveCard
                )
            },
            "board/{idBoard}/card/{idCard}" bind Method.DELETE to { request: Request ->
                handleRequest(
                    request,
                    webApi::deleteCard
                )
            },
            "board/{idBoard}/card/{idCard}/archive" bind Method.PUT to { request: Request ->
                handleRequest(
                    request,
                    webApi::archiveCard
                )
            },
            "board/{idBoard}/card/{idCard}/update" bind Method.PUT to { request: Request ->
                handleRequest(
                    request,
                    webApi::updateCard
                )
            }
        )
    }
}
