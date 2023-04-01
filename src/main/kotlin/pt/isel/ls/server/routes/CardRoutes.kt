package pt.isel.ls.server.routes

import org.http4k.core.Method
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import pt.isel.ls.server.api.CardAPI

class CardRoutes(private val webApi: CardAPI) {
    operator fun invoke(): RoutingHttpHandler {
        return routes(
            "board/{idBoard}/list/{idList}/card" bind Method.POST to webApi::createCard,
            "board/{idBoard}/list/{idList}/card" bind Method.GET to webApi::getCardsFromList,
            "board/{idBoard}/list/{idList}/card/{idCard}" bind Method.GET to webApi::getCard,
            "board/{idBoard}/list/{idList}/card/{idCard}" bind Method.PUT to webApi::moveCard /** idList destination comes in body.**/
            /** patch **/
        )
    }
}