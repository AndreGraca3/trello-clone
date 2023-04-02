package pt.isel.ls.tests.webApi

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import pt.isel.ls.server.utils.BoardList
import pt.isel.ls.server.utils.BoardListIn
import pt.isel.ls.tests.utils.app
import pt.isel.ls.tests.utils.baseUrl
import pt.isel.ls.tests.utils.boardId
import pt.isel.ls.tests.utils.createList
import pt.isel.ls.tests.utils.dataMem
import pt.isel.ls.tests.utils.dataSetup
import pt.isel.ls.tests.utils.dummyBoardListName
import pt.isel.ls.tests.utils.invalidToken
import pt.isel.ls.tests.utils.listId
import pt.isel.ls.tests.utils.user
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ListAPITests {

    @BeforeTest
    fun setup() {
        dataSetup(BoardList::class.java)
    }

    @Test
    fun `test create list`() {
        val list = BoardListIn(dummyBoardListName)

        val response = app(
            Request(
                Method.POST,
                "$baseUrl/board/$boardId/list"
            ).body(
                Json.encodeToString(list)
            ).header(
                "Authorization",
                "Bearer ${user.token}"
            )
        )

        val listOut = Json.decodeFromString<Int>(response.bodyString())

        assertEquals(0, listOut)
    }

    @Test
    fun `test create list without being logged`() {
        val response = app(
            Request(
                Method.POST,
                "$baseUrl/board/$boardId/list"
            ).body(
                Json.encodeToString(BoardListIn(dummyBoardListName))
            ).header(
                "Authorization",
                "Bearer $invalidToken"
            )
        )

        val msg = Json.decodeFromString<String>(response.bodyString())

        assertTrue(dataMem.listData.lists.isEmpty())
        assertEquals(response.status, Status.UNAUTHORIZED)
        assertEquals("Unauthorized Operation.", msg)
    }

    @Test
    fun `test create list without a body`() {
        val response = app(
            Request(
                Method.POST,
                "$baseUrl/board/$boardId/list"
            ).header(
                "Authorization",
                "Bearer ${user.token}"
            )
        )

        assertTrue(dataMem.listData.lists.isEmpty())
        assertEquals(Status.BAD_REQUEST, response.status)
    }

    @Test
    fun `test get list`() {
        val listId = createList(boardId)

        val response = app(
            Request(
                Method.GET,
                "$baseUrl/board/$boardId/list/$listId"
            ).header(
                "Authorization",
                "Bearer ${user.token}"
            )
        )

        val listOut = Json.decodeFromString<BoardList>(response.bodyString())

        assertEquals(0, listOut.idList)
        assertEquals(dummyBoardListName, listOut.name)
        assertEquals(boardId, listOut.idBoard)
        assertEquals(response.status, Status.OK)
    }

    @Test
    fun `test get all lists from board`() {
        val listsAmount = 3
        repeat(listsAmount) { createList(boardId) }

        val response = app(
            Request(
                Method.GET,
                "$baseUrl/board/$boardId/list"
            ).header(
                "Authorization",
                "Bearer ${user.token}"
            )
        )

        val fetchedLists = Json.decodeFromString<List<BoardList>>(response.bodyString())

        assertEquals(listsAmount, fetchedLists.size)
        fetchedLists.forEachIndexed { i, it ->
            assertEquals(boardId, it.idBoard)
            assertEquals(i, it.idList)
        }
        assertEquals(response.status, Status.OK)
    }

    @Test
    fun `test get lists from empty board`() {
        val response = app(
            Request(
                Method.GET,
                "$baseUrl/board/$boardId/list"
            ).header(
                "Authorization",
                "Bearer ${user.token}"
            )
        )

        val listsOut = Json.decodeFromString<List<BoardList>>(response.bodyString())

        assertTrue(listsOut.isEmpty())
        assertEquals(response.status, Status.OK)
    }

    @Test
    fun `test get details of list without being logged`() {
        val listId = createList(boardId)

        val response = app(
            Request(
                Method.GET,
                "$baseUrl/board/$boardId/list/$listId"
            ).header(
                "Authorization",
                "Bearer $invalidToken"
            )
        )

        assertEquals(Status.UNAUTHORIZED, response.status)
    }

    @Test
    fun `test get non-existing list`() {
        val response = app(
            Request(
                Method.GET,
                "$baseUrl/board/$boardId/list/$listId"
            ).header(
                "Authorization",
                "Bearer ${user.token}"
            )
        )

        val msg = Json.decodeFromString<String>(response.bodyString())

        assertEquals(Status.NOT_FOUND, response.status)
        assertEquals("BoardList not found.", msg)
    }

    @Test
    fun `test get all lists from board with no lists`() {
        val response = app(
            Request(Method.GET, "$baseUrl/board/$boardId/list")
                .header("Authorization", user.token)
        )

        val lists = Json.decodeFromString<List<BoardList>>(response.bodyString())

        assertTrue(lists.isEmpty())
        assertEquals(Status.OK, response.status)
    }
}
