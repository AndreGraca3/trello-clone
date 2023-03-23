package pt.isel.ls.server.web.api

import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import pt.isel.ls.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.http4k.core.Status
import org.http4k.routing.path
import pt.isel.ls.server.annotations.Auth
import pt.isel.ls.server.createRsp
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.services.CardServices
import pt.isel.ls.server.services.ListServices
import pt.isel.ls.server.services.UserServices

class CardAPI(private val services: CardServices) {

    fun createCard(request: Request): Response {
        return handleRequest(request, ::createCardInternal)
    }

    fun getCard(request: Request): Response {
        return handleRequest(request, ::getCardInternal)
    }

    fun getCardsFromList(request: Request): Response {
        return handleRequest(request, ::getCardsFromListInternal)
    }

    fun moveCard(request: Request): Response {
        return handleRequest(request, ::moveCardInternal)
    }


    @Auth
    private fun createCardInternal(request: Request, token: String): Response {
        val idList = request.path("idList")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idList")
        val newCard = Json.decodeFromString<CardIn>(request.bodyString())
        return createRsp(
            Status.CREATED,
            services.createCard(token, idList, newCard.name, newCard.description, newCard.endDate)
        )
    }

    @Auth
    private fun getCardInternal(request: Request, token: String): Response {
        val idCard = request.path("idCard")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idCard")
        val card = services.getCard(token, idCard)
        return createRsp(OK, CardOut(card.name, card.description, card.startDate, card.endDate, card.archived))
    }

    @Auth
    private fun getCardsFromListInternal(request: Request, token: String): Response {
        val idList = request.path("idList")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idList")
        return createRsp(OK, services.getCardsFromList(token, idList))
    }

    @Auth
    private fun moveCardInternal(request: Request, token: String): Response {
        val idListDst = Json.decodeFromString<Int>(request.bodyString())
        val idCard = request.path("idCard")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idCard")
        return createRsp(OK, services.moveCard(token, idCard, idListDst))
    }

}