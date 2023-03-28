package pt.isel.ls.utils.webApi

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.*
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import pt.isel.ls.*
import pt.isel.ls.server.data.initialState
import pt.isel.ls.utils.*

class WebApiCardsTests {

    /** Every Test Starts with **/
    @BeforeTest
    fun dataSetup() {
        initialState()
    }

    @Test
    fun `test create card without endDate`(){
        val userIn = dataUser.createUser(dummyName, dummyEmail)
        val idBoard = dataBoard.createBoard(userIn.first, dummyBoardName, dummyBoardDescription)
        val idList = dataList.createList(idBoard, dummyBoardListName)
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
    fun `test get card`(){
        val userIn = dataUser.createUser(dummyName, dummyEmail)
        val idBoard = dataBoard.createBoard(userIn.first, dummyBoardName, dummyBoardDescription)
        val idList = dataList.createList(idBoard, dummyBoardListName)
        val idCard = dataCard.createCard(idList, dummyCardName, dummyCardDescription, null)

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
    fun `test move card`(){
        val userIn = dataUser.createUser(dummyName, dummyEmail)
        val idBoard = dataBoard.createBoard(userIn.first, dummyBoardName, dummyBoardDescription)
        val idList = dataList.createList(idBoard, dummyBoardListName)
        val idCard = dataCard.createCard(idList, dummyCardName, dummyCardDescription, null)
        val idListDst = dataList.createList(idBoard, dummyBoardListName)

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