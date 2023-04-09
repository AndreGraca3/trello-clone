package pt.isel.ls.server.api

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import pt.isel.ls.server.annotations.Auth
import pt.isel.ls.server.services.CardServices
import pt.isel.ls.server.utils.CardIn
import pt.isel.ls.server.utils.CardOut
import pt.isel.ls.server.utils.NewList

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

    fun deleteCard(request: Request): Response {
        return handleRequest(request, ::deleteCardInternal)
    }

    @Auth
    private fun createCardInternal(request: Request, token: String): Response {
        val idBoard = getPathParam(request, "idBoard")
        val idList = getPathParam(request, "idList")
        val newCard = Json.decodeFromString<CardIn>(request.bodyString())
        return createRsp(
            Status.CREATED,
            services.createCard(token, idBoard, idList, newCard.name, newCard.description, newCard.endDate)
        )
    }

    @Auth
    private fun getCardInternal(request: Request, token: String): Response {
        val idBoard = getPathParam(request, "idBoard")
        val idList = getPathParam(request, "idList")
        val idCard = getPathParam(request, "idCard")
        val card = services.getCard(token, idBoard, idList, idCard)
        return createRsp(Status.OK, CardOut(card.name, card.description, card.startDate, card.endDate, card.archived))
    }

    @Auth
    private fun getCardsFromListInternal(request: Request, token: String): Response {
        val idBoard = getPathParam(request, "idBoard")
        val idList = getPathParam(request, "idList")
        return createRsp(Status.OK, services.getCardsFromList(token, idBoard, idList))
    }

    @Auth
    private fun moveCardInternal(request: Request, token: String): Response {
        val idBoard = getPathParam(request, "idBoard")
        val idListNow = getPathParam(request, "idList")
        val objIdListDst = Json.decodeFromString<NewList>(request.bodyString())
        val idCard = getPathParam(request, "idCard")
        return createRsp(Status.OK, services.moveCard(token, idBoard, idListNow, objIdListDst.idList, idCard, objIdListDst.cix))
    }

    @Auth
    private fun deleteCardInternal(request: Request, token: String): Response {
        val idBoard = getPathParam(request, "idBoard")
        val idList = getPathParam(request, "idList")
        val idCard = getPathParam(request, "idCard")
        return createRsp(Status.NO_CONTENT, services.deleteCard(token, idBoard, idList, idCard))
    }
}
