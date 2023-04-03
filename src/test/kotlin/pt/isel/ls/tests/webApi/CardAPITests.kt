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
import pt.isel.ls.server.utils.IDList
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
import pt.isel.ls.tests.utils.listId
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
    fun `test move card`() {
        val idCard = createCard(listId, boardId, dummyCardName, dummyCardDescription)
        val idListDst = createList(boardId, dummyBoardListName + "2")

        val requestBody = Json.encodeToString(IDList(idListDst))

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

        val requestBody = Json.encodeToString(IDList(idListDst))

        val response = app(
            Request(
                Method.PUT,
                "$baseUrl/board/$boardId/list/$listId/card/$idCard"
            ).body(requestBody)
        )

        assertEquals(Status.UNAUTHORIZED, response.status)
    }
}
