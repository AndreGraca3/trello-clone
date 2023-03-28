package pt.isel.ls.utils.webApi

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test
import pt.isel.ls.Board
import pt.isel.ls.BoardIn
import pt.isel.ls.BoardOut
import pt.isel.ls.server.data.boards
import pt.isel.ls.server.data.initialState
import pt.isel.ls.utils.app
import pt.isel.ls.utils.baseUrl
import pt.isel.ls.utils.dataBoard
import pt.isel.ls.utils.dataUser
import pt.isel.ls.utils.dummyBoardDescription
import pt.isel.ls.utils.dummyBoardName
import pt.isel.ls.utils.dummyEmail
import pt.isel.ls.utils.dummyName
import pt.isel.ls.utils.invalidToken
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class WebApiBoardTests {

    /** Every Test Starts with **/
    @BeforeTest
    fun dataSetup() {
        initialState()
    }

    @Test
    fun `test create board while logged in`() {
        val token = dataUser.createUser(dummyName, dummyEmail).second

        val boardIn = Json.encodeToString(BoardIn(dummyBoardName, dummyBoardDescription))

        val response = app(
            Request(
                Method.POST,
                "$baseUrl/board"
            ).header(
                "Authorization",
                "Bearer $token"
            ).body(boardIn)
        )

        assertEquals(Status.CREATED, response.status)

        val board = Json.decodeFromString<BoardOut>(response.bodyString())

        assertEquals(0, board.idBoard)
    }

    @Test
    fun `test create board without being logged`() {
        val boardIn = Json.encodeToString(BoardIn(dummyBoardName, dummyBoardDescription))

        val response = app(
            Request(Method.POST, "$baseUrl/board")
                .header("Authorization", "Bearer $invalidToken")
                .body(boardIn)
        )

        val msg = Json.decodeFromString<String>(response.bodyString())

        assertTrue(boards.isEmpty())

        /** verify **/
        assertEquals(Status.UNAUTHORIZED, response.status)
        assertEquals("Unauthorized Operation.", msg)
    }

    @Test
    fun `test create board without a body`() {
        val token = createUser().second

        val response = app(
            Request(Method.POST, "$baseUrl/board")
                .header("Authorization", "Bearer $token")
        )

        val msg = Json.decodeFromString<String>(response.bodyString())

        assertTrue(boards.isEmpty())

        assertEquals(Status.BAD_REQUEST, response.status)
    }

    @Test
    fun `test get all boards while logged in`() {
        val user = dataUser.createUser(dummyName, dummyEmail)
        val names = listOf("board1", "board2", "board3")
        for (i in 0..2) {
            dataBoard.createBoard(user.first, names[i], dummyBoardDescription)
        }

        val boardData = boards

        val response = app(
            Request(
                Method.GET,
                "$baseUrl/board"
            ).header(
                "Authorization",
                user.second
            )
        )

        assertEquals(response.status, Status.OK)

        val boards = Json.decodeFromString<List<Board>>(response.bodyString())

        assertEquals(boards, boardData)
    }

    @Test
    fun `test get all boards without being logged in`() {
        val user = createUser()
        val names = listOf("Board1", "board2", "board3")
        for (i in 0..2) {
            dataBoard.createBoard(user.first, names[i], dummyBoardDescription)
        }

        val response = app(
            Request(Method.GET, "$baseUrl/board")
                .header("Authorization", invalidToken)
        )

        /** verify **/
        assertEquals(Status.UNAUTHORIZED, response.status)
    }

    @Test
    fun `test get all boards from user with no boards`() {
        val user = createUser()

        val response = app(
            Request(Method.GET, "$baseUrl/board")
                .header("Authorization", user.second)
        )

        val boardData = boards

        val boards = Json.decodeFromString<List<Board>>(response.bodyString())

        assertTrue(boards.isEmpty())

        assertEquals(Status.OK, response.status)
        assertEquals(boardData, boards)
    }

    @Test
    fun `test add user to board while logged in`() {
        val user1 = dataUser.createUser(dummyName, dummyEmail)
        val boardId = dataBoard.createBoard(user1.first, dummyBoardName, dummyBoardDescription)

        val boardsData = boards

        val dummyName2 = "Diogo"
        val dummyEmail2 = "Diogo@gmail.com"
        val user2 = dataUser.createUser(dummyName2, dummyEmail2)
        val userId2 = Json.encodeToString(user2.first)

        val response = app(
            Request(
                Method.PUT,
                "$baseUrl/board/$boardId"
            ).header(
                "Authorization",
                user1.second
            ).body(userId2)
        )

        assertEquals(Status.OK, response.status)

        assertTrue(boardsData.first().idUsers.contains(user2.first))
    }

    @Test
    fun `test add user to board without being logged in`() {
        val user1 = createUser()
        val boardId = dataBoard.createBoard(user1.first, dummyBoardName, dummyBoardDescription)

        val dummyName2 = "Diogo"
        val dummyEmail2 = "Diogo@gmail.com"
        val user2 = dataUser.createUser(dummyName2, dummyEmail2)
        val userId2 = Json.encodeToString(user2.first)

        val boardData = boards

        val response = app(
            Request(Method.PUT, "$baseUrl/board/$boardId")
                .header("Authorization", invalidToken)
                .body(userId2)
        )

        val msg = Json.decodeFromString<String>(response.bodyString())

        /** verify **/
        assertEquals(Status.UNAUTHORIZED, response.status)
        assertEquals("Unauthorized Operation.", msg)

        assertFalse(boardData.first().idUsers.contains(user2.first))
    }

    @Test
    fun `test get details of board while logged in`() {
        val user = dataUser.createUser(dummyName, dummyEmail)
        val boardId = dataBoard.createBoard(user.first, dummyBoardName, dummyBoardDescription)

        val response = app(
            Request(
                Method.GET,
                "$baseUrl/board/$boardId"
            ).header(
                "Authorization",
                user.second
            )
        )

        assertEquals(Status.OK, response.status)

        val board = Json.decodeFromString<Board>(response.bodyString())

        assertEquals(
            Board(boardId, dummyBoardName, dummyBoardDescription, mutableListOf(user.first)),
            board
        )
    }

    @Test
    fun `test get details of board without being logged in`() {
        val user = createUser()
        val boardId = dataBoard.createBoard(user.first, dummyBoardName, dummyBoardDescription)

        val response = app(
            Request(Method.GET, "$baseUrl/board/$boardId")
                .header("Authorization", invalidToken)
        )

        val msg = Json.decodeFromString<String>(response.bodyString())

        assertEquals(Status.UNAUTHORIZED, response.status)
        assertEquals("Unauthorized Operation.", msg)
    }

    @Test
    fun `test get details of a non-existing board`() {
        val user = createUser()
        val boardData = boards
        assertTrue(boardData.isEmpty())

        val response = app(
            Request(Method.GET, "$baseUrl/board/0")
                .header("Authorization", user.second)
        )

        val msg = Json.decodeFromString<String>(response.bodyString())

        assertEquals(Status.NOT_FOUND, response.status)
        assertEquals("Board not found.", msg)
    }

    private fun createUser(): Pair<Int, String> {
        return dataUser.createUser(dummyName, dummyEmail)
    }
}
