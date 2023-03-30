package pt.isel.ls.server.api

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.path
import pt.isel.ls.server.Services
import pt.isel.ls.server.annotations.Auth
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.CardIn
import pt.isel.ls.server.utils.CardOut

class CardWebApi(private val services: Services) {

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
    private fun createCardInternal(
        request: Request,
        token: String
    ): Response { // Why is this POST and not PUT like createList?
        val idBoard = request.path("idBoard")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idBoard")
        val idList = request.path("idList")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idList")
        val newCard = Json.decodeFromString<CardIn>(request.bodyString())
        return createRsp(
            Status.CREATED,
            services.createCard(token, idList, idBoard, newCard.name, newCard.description, newCard.endDate)
        )
    }

    @Auth
    private fun getCardInternal(request: Request, token: String): Response {
        val idBoard = request.path("idBoard")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idBoard")
        val idList = request.path("idList")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idList")
        val idCard = request.path("idCard")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idCard")
        val card = services.getCard(token, idBoard, idList, idCard)
        return createRsp(Status.OK, CardOut(card.name, card.description, card.startDate, card.endDate, card.archived))
    }

    @Auth
    private fun getCardsFromListInternal(request: Request, token: String): Response {
        val idBoard = request.path("idBoard")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idBoard")
        val idList = request.path("idList")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idList")
        return createRsp(Status.OK, services.getCardsFromList(token, idBoard, idList))
    }

    @Auth
    private fun moveCardInternal(request: Request, token: String): Response {
        val idBoard = request.path("idBoard")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idBoard")
        val idListNow = request.path("idList")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idList")
        val idListDst = Json.decodeFromString<Int>(request.bodyString())
        val idCard = request.path("idCard")?.toIntOrNull() ?: throw TrelloException.IllegalArgument("idCard")
        return createRsp(Status.OK, services.moveCard(token, idCard, idBoard, idListNow, idListDst))
    }


}