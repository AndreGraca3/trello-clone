package pt.isel.ls.tests.webApi

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import pt.isel.ls.server.utils.Board
import pt.isel.ls.server.utils.BoardIn
import pt.isel.ls.server.utils.BoardOut
import pt.isel.ls.server.utils.IDUser
import pt.isel.ls.tests.utils.*
import kotlin.test.*

class BoardAPITests {

    @BeforeTest
    fun setup() {
        dataSetup(Board::class.java)
    }


    @Test
    fun `test create board while logged in`() {
        val boardIn = Json.encodeToString(BoardIn(dummyBoardName, dummyBoardDescription))

        val response = app(
            Request(
                Method.POST,
                "$baseUrl/board"
            ).header(
                "Authorization",
                "Bearer ${user.token}"
            ).body(boardIn)
        )

        val board = Json.decodeFromString<BoardOut>(response.bodyString())

        assertEquals(Status.CREATED, response.status)
        assertEquals(0, board.idBoard)
    }

    @Test
    fun `test create board without being logged`() {
        val boardIn = Json.encodeToString(BoardIn(dummyBoardName, dummyBoardDescription))

        val response = app(
            Request(Method.POST, "$baseUrl/board")
                .body(boardIn)
        )

        val msg = Json.decodeFromString<String>(response.bodyString())

        assertTrue(dataMem.boardData.boards.isEmpty())
        assertEquals(Status.UNAUTHORIZED, response.status)
        assertEquals("Unauthorized Operation.", msg)
    }

    @Test
    fun `test create board without a body`() {

        val response = app(
            Request(Method.POST, "$baseUrl/board")
                .header("Authorization", "Bearer ${user.token}")
        )

        assertTrue(dataMem.boardData.boards.isEmpty())
        assertEquals(Status.BAD_REQUEST, response.status)
    }

    @Test
    fun `test get all boards while logged in`() {
        val boardsAmount = 3
        repeat(boardsAmount) {
            dataMem.boardData.createBoard(user.idUser, dummyBoardName + it, dummyBoardDescription)
        }

        val response = app(
            Request(
                Method.GET,
                "$baseUrl/board"
            ).header(
                "Authorization",
                user.token
            )
        )

        val fetchedBoards = Json.decodeFromString<List<Board>>(response.bodyString())

        assertEquals(boardsAmount, fetchedBoards.size)
        fetchedBoards.forEachIndexed { i, it ->
            assertTrue(dataMem.userBoardData.usersBoards.any{ it.idUser == user.idUser })
            assertEquals(i, it.idBoard)
            assertEquals(dummyBoardName + i, it.name)
        }
        assertEquals(response.status, Status.OK)
    }

    @Test
    fun `test get all boards without being logged in`() {
        repeat (3) {
            dataMem.boardData.createBoard(user.idUser, dummyBoardName + it, dummyBoardDescription)
        }

        val response = app(
            Request(Method.GET, "$baseUrl/board")
                .header("Authorization", invalidToken)
        )

        assertEquals(Status.UNAUTHORIZED, response.status)
    }

    @Test
    fun `test get all boards from user with no boards`() {
        val response = app(
            Request(Method.GET, "$baseUrl/board")
                .header("Authorization", user.token)
        )

        val boards = Json.decodeFromString<List<Board>>(response.bodyString())

        assertTrue(boards.isEmpty())
        assertEquals(Status.OK, response.status)
    }

    @Test
    fun `test get details of board while logged in`() {
        val boardId = createBoard(user.idUser)

        val response = app(
            Request(
                Method.GET,
                "$baseUrl/board/$boardId"
            ).header(
                "Authorization",
                user.token
            )
        )

        val board = Json.decodeFromString<Board>(response.bodyString())

        assertEquals(boardId, board.idBoard)
        assertTrue(dataMem.userBoardData.usersBoards.any { it.idUser == user.idUser })
        assertEquals(Status.OK, response.status)
    }

    @Test
    fun `test get details of board without being logged in`() {
        val boardId = createBoard(user.idUser)

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

        val response = app(
            Request(Method.GET, "$baseUrl/board/0")
                .header("Authorization", user.token)
        )

        val msg = Json.decodeFromString<String>(response.bodyString())

        assertEquals(Status.NOT_FOUND, response.status)
        assertEquals("Board not found.", msg)
    }

    @Test
    fun `test add user to board while logged in`() {
        val boardId = createBoard(user.idUser)

        val dummyName2 = "Diogo"
        val dummyEmail2 = "Diogo@gmail.com"
        val user2 = createUser(dummyName2, dummyEmail2)
        val userId2 = Json.encodeToString(IDUser(user2.first))

        val response = app(
            Request(
                Method.PUT,
                "$baseUrl/board/$boardId"
            ).header(
                "Authorization",
                user.token
            ).body(userId2)
        )

        assertEquals(dataMem.userBoardData.usersBoards.last().idUser, user2.first)
        assertEquals(Status.OK, response.status)
    }

    @Test
    fun `test add user to board without being logged in`() {
        val boardId = createBoard(user.idUser)

        val dummyName2 = "Diogo"
        val dummyEmail2 = "Diogo@gmail.com"
        val user2 = createUser(dummyName2, dummyEmail2)
        val userId2 = Json.encodeToString(user2.first)

        val response = app(
            Request(Method.PUT, "$baseUrl/board/$boardId")
                .header("Authorization", invalidToken)
                .body(userId2)
        )

        val msg = Json.decodeFromString<String>(response.bodyString())

        assertNotEquals(dataMem.userBoardData.usersBoards.last().idUser, user2.first)
        assertEquals(Status.UNAUTHORIZED, response.status)
        assertEquals("Unauthorized Operation.", msg)
    }
}
