package pt.isel.ls.tests.webApi

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import pt.isel.ls.server.utils.Card
import pt.isel.ls.server.utils.CardIn
import pt.isel.ls.server.utils.CardOut
import pt.isel.ls.server.utils.NewList
import pt.isel.ls.tests.utils.app
import pt.isel.ls.tests.utils.baseUrl
import pt.isel.ls.tests.utils.boardId
import pt.isel.ls.tests.utils.cardId
import pt.isel.ls.tests.utils.createCard
import pt.isel.ls.tests.utils.createList
import pt.isel.ls.tests.utils.dataMem
import pt.isel.ls.tests.utils.dataSetup
import pt.isel.ls.tests.utils.dummyBoardListName
import pt.isel.ls.tests.utils.dummyCardDescription
import pt.isel.ls.tests.utils.dummyCardName
import pt.isel.ls.tests.utils.invalidToken
import pt.isel.ls.tests.utils.listId
import pt.isel.ls.tests.utils.services
import pt.isel.ls.tests.utils.user
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CardAPITests {

    @BeforeTest
    fun setup() {
        dataSetup(Card::class.java)
    }

    @Test
    fun `test create card without endDate`() {
        val cardIn = CardIn(dummyCardName, dummyCardDescription, null)

        val requestBody = Json.encodeToString(cardIn)

        val response = app(
            Request(
                Method.POST,
                "$baseUrl/board/$boardId/list/$listId/card"
            ).header(
                "Authorization",
                "Bearer ${user.token}"
            ).body(
                requestBody
            )
        )

        assertEquals(Status.CREATED, response.status)
        assertEquals("application/json", response.header("content-type"))

        val bodyString = response.bodyString()

        val cardOut = Json.decodeFromString<Int>(bodyString)
        assertEquals(0, cardOut)
    }

    @Test
    fun `create card without being logged`() {
        val cardIn = CardIn(dummyCardName, dummyCardDescription, null)

        val requestBody = Json.encodeToString(cardIn)

        val response = app(
            Request(
                Method.POST,
                "$baseUrl/board/$boardId/list/$listId/card"
            ).body(
                requestBody
            )
        )

        assertEquals(Status.UNAUTHORIZED, response.status)
    }

    @Test
    fun `test create card without a body`() {
        val response = app(
            Request(
                Method.POST,
                "$baseUrl/board/$boardId/list"
            ).header(
                "Authorization",
                "Bearer ${user.token}"
            )
        )

        assertTrue(dataMem.cardData.cards.isEmpty())
        assertEquals(Status.BAD_REQUEST, response.status)
    }

    @Test
    fun `test get card`() {
        val idCard = createCard(listId, boardId, dummyCardName, dummyCardDescription)

        val response = app(
            Request(
                Method.GET,
                "$baseUrl/board/$boardId/list/$listId/card/$idCard"
            ).header(
                "Authorization",
                "Bearer ${user.token}"
            )
        )

        assertEquals(Status.OK, response.status)
        assertEquals("application/json", response.header("content-type"))

        val cardOut = Json.decodeFromString<CardOut>(response.bodyString())
        assertEquals(dummyCardName, cardOut.name)
        assertEquals(dummyCardDescription, cardOut.description)
    }

    @Test
    fun `test get card without being logged`() {
        val idCard = createCard(listId, boardId, dummyCardName, dummyCardDescription)

        val response = app(
            Request(
                Method.GET,
                "$baseUrl/board/$boardId/list/$listId/card/$idCard"
            )
        )

        assertEquals(Status.UNAUTHORIZED, response.status)
    }

    @Test
    fun `test get non-existent card`() {
        val response = app(
            Request(
                Method.GET,
                "$baseUrl/board/$boardId/list/$listId/card/$cardId"
            ).header(
                "Authorization",
                "Bearer ${user.token}"
            )
        )

        val msg = Json.decodeFromString<String>(response.bodyString())

        assertEquals(Status.NOT_FOUND, response.status)
        assertEquals("Card not found.", msg)
    }

    @Test
    fun `test move card while logged in`() {
        val idCard = createCard(listId, boardId, dummyCardName, dummyCardDescription)
        val idListDst = createList(boardId, dummyBoardListName + "2")

        val requestBody = Json.encodeToString(NewList(idListDst, 1))

        val response = app(
            Request(
                Method.PUT,
                "$baseUrl/board/$boardId/list/$listId/card/$idCard"
            ).header(
                "Authorization",
                "Bearer ${user.token}"
            ).body(requestBody)
        )

        assertEquals(Status.OK, response.status)
        assertEquals("application/json", response.header("content-type"))
    }

    @Test
    fun `test move card without being logged`() {
        val idCard = createCard(listId, boardId, dummyCardName, dummyCardDescription)
        val idListDst = createList(boardId, dummyBoardListName + "2")

        val requestBody = Json.encodeToString(NewList(idListDst, 0))

        val response = app(
            Request(
                Method.PUT,
                "$baseUrl/board/$boardId/list/$listId/card/$idCard"
            ).body(requestBody)
        )

        assertEquals(Status.UNAUTHORIZED, response.status)
    }

    @Test
    fun `test move card from a list with cards to a list with cards`() {
        val idCard = 1
        val idListDst = createList(boardId, dummyBoardListName + "2")

        for (i in 1..6) {
            if (i <= 3) createCard(listId, boardId, "card$i", "this is a card$i.", null)
            if (i > 3) createCard(idListDst, boardId, "card$i", "this is a card$i.", null)
        }

        assertEquals(3, dataMem.cardData.getCardsFromList(listId, boardId, 3, 0).size)
        assertEquals(3, dataMem.cardData.getCardsFromList(idListDst, boardId, 3, 0).size)

        val requestBody = Json.encodeToString(NewList(idListDst, 2))

        val response = app(
            Request(
                Method.PUT,
                "$baseUrl/board/$boardId/list/$listId/card/$idCard"
            ).header(
                "Authorization",
                "Bearer ${user.token}"
            ).body(requestBody)
        )

        assertEquals(Status.OK, response.status)

        val listSrc = dataMem.cardData.getCardsFromList(listId, boardId, 2, 0)
        val listDst = dataMem.cardData.getCardsFromList(idListDst, boardId, 4, 0)

        assertEquals(2, listSrc.size)
        assertEquals(4, listDst.size)

        listSrc.forEach { assertEquals(it.idx, listSrc.indexOf(it) + 1) }

        listDst.forEach { assertEquals(it.idx, listDst.indexOf(it) + 1) }
    }

    @Test
    fun `test move card to other index of the same list`() {
        val idCard = 1

        for (i in 1..6) {
            createCard(listId, boardId, "card$i", "this is a card$i.", null)
        }

        assertEquals(6, dataMem.cardData.getCardsFromList(listId, boardId, 6, 0).size)

        val requestBody = Json.encodeToString(NewList(listId, 4))

        val response = app(
            Request(
                Method.PUT,
                "$baseUrl/board/$boardId/list/$listId/card/$idCard"
            ).header(
                "Authorization",
                "Bearer ${user.token}"
            ).body(requestBody)
        )

        assertEquals(Status.OK, response.status)

        val listSrc = dataMem.cardData.getCardsFromList(listId, boardId, 6, 0)

        assertEquals(6, listSrc.size)

        listSrc.forEach { assertEquals(it.idx, listSrc.indexOf(it) + 1) }
    }

    @Test
    fun `get cards from list with valid pagination`() {
        val skip = 2
        val limit = 3

        repeat(6) {
            services.cardServices.createCard(user.token, boardId, listId, "card$it", "card$it", null)
        }

        val response = app(
            Request(Method.GET, "$baseUrl/board/$boardId/list/$listId/card?skip=$skip&limit=$limit")
                .header("Authorization", user.token)
        )

        val cards = Json.decodeFromString<List<Card>>(response.bodyString())

        assertEquals(dataMem.cardData.cards.subList(skip, skip + limit), cards)
    }

    @Test
    fun `get boards from user with invalid pagination negative numbers`() {
        val skip = -2
        val limit = -2

        repeat(6) {
            services.cardServices.createCard(user.token, boardId, listId, "card$it", "card$it", null)
        }

        val response = app(
            Request(Method.GET, "$baseUrl/board/$boardId/list/$listId/card?skip=$skip&limit=$limit")
                .header("Authorization", user.token)
        )

        val cards = Json.decodeFromString<List<Card>>(response.bodyString())

        assertEquals(dataMem.cardData.cards.subList(0, cards.size), cards)
    }

    @Test
    fun `get boards from user with invalid pagination bigger than size`() {
        val skip = 10
        val limit = 7

        repeat(6) {
            services.cardServices.createCard(user.token, boardId, listId, "card$it", "card$it", null)
        }

        val response = app(
            Request(Method.GET, "$baseUrl/board/$boardId/list/$listId/card?skip=$skip&limit=$limit")
                .header("Authorization", user.token)
        )

        val cards = Json.decodeFromString<List<Card>>(response.bodyString())

        assertEquals(dataMem.cardData.cards.subList(0, cards.size), cards)
    }

    @Test
    fun `delete a card while logged in`() {
        val idCard = createCard(listId, boardId, dummyCardName, dummyCardDescription)

        assertEquals(1, dataMem.cardData.cards.size)

        val response = app(
            Request(Method.DELETE, "$baseUrl/board/$boardId/list/$listId/card/$idCard")
                .header("Authorization", user.token)
        )

        assertEquals(Status.OK, response.status)
        assertEquals(0, dataMem.cardData.cards.size)
    }

    @Test
    fun `delete a card without being logged in`() {
        val idCard = createCard(listId, boardId, dummyCardName, dummyCardDescription)

        assertEquals(1, dataMem.cardData.cards.size)

        val response = app(
            Request(Method.DELETE, "$baseUrl/board/$boardId/list/$listId/card/$idCard")
                .header("Authorization", invalidToken)
        )

        assertEquals(Status.UNAUTHORIZED, response.status)
        assertEquals(1, dataMem.cardData.cards.size)
    }

    @Test
    fun `delete a card from a list that user doesn't belong to`() {
        val otherBoardId = 6
        val idCard = createCard(listId, boardId, dummyCardName, dummyCardDescription)
        val idList = createList(otherBoardId, dummyBoardListName)

        assertEquals(1, dataMem.cardData.cards.size)

        val response = app(
            Request(Method.DELETE, "$baseUrl/board/$otherBoardId/list/$idList/card/$idCard")
                .header("Authorization", user.token)
        )

        assertEquals(Status.NOT_FOUND, response.status)

        assertEquals(1, dataMem.cardData.cards.size)
    }

    @Test
    fun `delete a non-existing card`() {
        val invalidCardId = 2000
        val idCard = createCard(listId, boardId, dummyCardName, dummyCardDescription)
        assertEquals(1, dataMem.cardData.cards.size)

        val response = app(
            Request(Method.DELETE, "$baseUrl/board/$boardId/list/$listId/card/$invalidCardId")
                .header("Authorization", user.token)
        )

        assertEquals(Status.NO_CONTENT, response.status)
        assertEquals(1, dataMem.cardData.cards.size)
    }
}
