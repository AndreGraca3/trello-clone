package pt.isel.ls.server.api

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import pt.isel.ls.server.annotations.Auth
import pt.isel.ls.server.services.CardServices
import pt.isel.ls.server.utils.*

class CardAPI(private val services: CardServices) {

    @Auth
    fun createCard(request: Request, token: String): Response {
        val idBoard = getPathParam(request, "idBoard")
        val newCard = Json.decodeFromString<CardIn>(request.bodyString())
        return createRsp(
            Status.CREATED,
            services.createCard(token, idBoard, newCard.idList, newCard.name, newCard.description, newCard.endDate)
        )
    }

    @Auth
    fun getCard(request: Request, token: String): Response {
        val idBoard = getPathParam(request, "idBoard")
        //val idList = getPathParam(request, "idList")
        val idCard = getPathParam(request, "idCard")
        val card = services.getCard(token, idBoard, idCard)
        return createRsp(
            Status.OK,
            Card(
                card.idCard,
                card.idList,
                card.idBoard,
                card.name,
                card.description,
                card.startDate,
                card.endDate,
                card.archived,
                card.idx
            )
        )
    }

    @Auth
    fun moveCard(request: Request, token: String): Response {
        val idBoard = getPathParam(request, "idBoard")
        //val idListNow = getPathParam(request, "idList")
        val params = Json.decodeFromString<NewList>(request.bodyString())
        val idCard = getPathParam(request, "idCard")
        return createRsp(
            Status.OK,
            services.moveCard(token, idBoard, params.idListNow, params.idListDst, idCard, params.cix)
        )
    }

    @Auth
    fun deleteCard(request: Request, token: String): Response {
        val idBoard = getPathParam(request, "idBoard")
        //val idList = getPathParam(request, "idList")
        val idCard = getPathParam(request, "idCard")
        return createRsp(Status.OK, services.deleteCard(token, idBoard, idCard))
    }

    @Auth
    fun archiveCard(request: Request, token: String): Response {
        val idBoard = getPathParam(request, "idBoard")
        val idCard = getPathParam(request, "idCard")
        return createRsp(
            Status.OK,
            services.archiveCard(token, idBoard, idCard)
        )
    }

    @Auth
    fun updateCard(request: Request, token: String): Response {
        val idBoard = getPathParam(request, "idBoard")
        val idCard = getPathParam(request, "idCard")
        val changes = Json.decodeFromString<Changes>(request.bodyString())
        return createRsp(
            Status.OK,
            services.updateCard(token, idBoard, idCard, changes.description, changes.endDate)
        )
    }
}
