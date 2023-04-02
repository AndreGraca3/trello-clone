package pt.isel.ls.tests.webApi

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import pt.isel.ls.server.utils.CardIn
import pt.isel.ls.server.utils.CardOut
import pt.isel.ls.tests.utils.app
import pt.isel.ls.tests.utils.baseUrl
import pt.isel.ls.tests.utils.createBoard
import pt.isel.ls.tests.utils.createCard
import pt.isel.ls.tests.utils.createList
import pt.isel.ls.tests.utils.createUser
import pt.isel.ls.tests.utils.dummyBoardDescription
import pt.isel.ls.tests.utils.dummyBoardListName
import pt.isel.ls.tests.utils.dummyBoardName
import pt.isel.ls.tests.utils.dummyCardDescription
import pt.isel.ls.tests.utils.dummyCardName
import pt.isel.ls.tests.utils.dummyEmail
import pt.isel.ls.tests.utils.dummyName
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CardAPITests {

    @BeforeTest
    fun setup() {
        TODO("use Aux functions and consts")
    }

    @Test
    fun `test create card without endDate`() {
        TODO()
        val userIn = createUser(dummyName, dummyEmail)
        val idBoard = createBoard(userIn.first, dummyBoardName, dummyBoardDescription)
        val idList = createList(idBoard, dummyBoardListName)
        val cardIn = CardIn(dummyCardName, dummyCardDescription, null)

        val requestBody = Json.encodeToString(cardIn)

        val response = app(
            Request(
                Method.POST,
                "$baseUrl/board/$idBoard/list/$idList/card"
            ).header(
                "Authorization",
                "Bearer ${userIn.second}"
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
    fun `test get card`() {
        TODO()
        val userIn = createUser(dummyName, dummyEmail)
        val idBoard = createBoard(userIn.first, dummyBoardName, dummyBoardDescription)
        val idList = createList(idBoard, dummyBoardListName)
        val idCard = createCard(idList, idBoard, dummyCardName, dummyCardDescription)

        val response = app(
            Request(
                Method.GET,
                "$baseUrl/board/$idBoard/list/$idList/card/$idCard"
            ).header(
                "Authorization",
                "Bearer ${userIn.second}"
            )
        )

        assertEquals(Status.OK, response.status)
        assertEquals("application/json", response.header("content-type"))

        val cardOut = Json.decodeFromString<CardOut>(response.bodyString())
        assertEquals(dummyCardName, cardOut.name)
        assertEquals(dummyCardDescription, cardOut.description)
    }

    @Test
    fun `test move card`() {
        TODO()
        val userIn = createUser(dummyName, dummyEmail)
        val idBoard = createBoard(userIn.first, dummyBoardName, dummyBoardDescription)
        val idList = createList(idBoard, dummyBoardListName)
        val idCard = createCard(idList, idBoard, dummyCardName, dummyCardDescription, null)
        val idListDst = createList(idBoard, dummyBoardListName)

        val requestBody = Json.encodeToString(idListDst)

        val response = app(
            Request(
                Method.PUT,
                "$baseUrl/board/$idBoard/list/$idList/card/$idCard"
            ).header(
                "Authorization",
                "Bearer ${userIn.second}"
            ).body(requestBody)
        )

        assertEquals(Status.OK, response.status)
        assertEquals("application/json", response.header("content-type"))
    }
}
