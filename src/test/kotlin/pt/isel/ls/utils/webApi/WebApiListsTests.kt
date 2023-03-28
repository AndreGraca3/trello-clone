package pt.isel.ls.utils.webApi

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.*
import org.http4k.core.Method
import org.http4k.core.Request
import pt.isel.ls.*
import pt.isel.ls.server.data.initialState
import pt.isel.ls.utils.*

class WebApiListsTests {

    /** Every Test Starts with **/
    @BeforeTest
    fun dataSetup() {
        initialState()
    }

    @Test
    fun `test create list`() {
        val user = dataUser.createUser(dummyName, dummyEmail)
        val idBoard = dataBoard.createBoard(user.first, dummyBoardName, dummyBoardDescription)

        val list = BoardListIn(dummyBoardListName)

        val response = app(
            Request(
                Method.POST,
                "$baseUrl/board/$idBoard/list"
            ).body(
                Json.encodeToString(list)
            ).header(
                "Authorization",
                "Bearer ${user.second}"
            )
        )

        val listOut = Json.decodeFromString<Int>(response.bodyString())

        assertEquals(0, listOut)
    }

    @Test
    fun `test get list`() {
        val user = dataUser.createUser(dummyName, dummyEmail)
        val idBoard = dataBoard.createBoard(user.first, dummyBoardName, dummyBoardDescription)
        val idList = dataList.createList(idBoard, dummyBoardListName)

        val response = app(
            Request(
                Method.GET,
                "$baseUrl/board/$idBoard/list/$idList"
            ).header(
                "Authorization",
                "Bearer ${user.second}"
            )
        )

        val listOut = Json.decodeFromString<BoardList>(response.bodyString())

        assertEquals(0, listOut.idList)
        assertEquals(dummyBoardListName, listOut.name)
        assertEquals(idBoard, listOut.idBoard)
    }

    @Test
    fun `test get all lists from board`() {
        val user = dataUser.createUser(dummyName, dummyEmail)
        val idBoard = dataBoard.createBoard(user.first, dummyBoardName, dummyBoardDescription)
        repeat(3) { dataList.createList(idBoard, dummyBoardListName) }

        val response = app(
            Request(
                Method.GET,
                "$baseUrl/board/$idBoard/list"
            ).header(
                "Authorization",
                "Bearer ${user.second}"
            )
        )

        val listsOut = Json.decodeFromString<List<BoardList>>(response.bodyString())

        val it = listsOut.iterator()

        while (it.hasNext()) {
            val elem = it.next()
            assertEquals(idBoard, elem.idBoard)
        }
    }
}