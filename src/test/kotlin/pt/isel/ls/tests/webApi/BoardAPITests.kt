package pt.isel.ls.tests.webApi

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import pt.isel.ls.server.Board
import pt.isel.ls.server.BoardDetailed
import pt.isel.ls.server.BoardIn
import pt.isel.ls.server.BoardOut
import pt.isel.ls.server.EmailUser
import pt.isel.ls.server.TotalBoards
import pt.isel.ls.server.User
import pt.isel.ls.server.data.dataMem.boards
import pt.isel.ls.server.data.dataMem.users
import pt.isel.ls.server.data.dataMem.usersBoards
import pt.isel.ls.tests.utils.app
import pt.isel.ls.tests.utils.baseUrl
import pt.isel.ls.tests.utils.boardId
import pt.isel.ls.tests.utils.createBoard
import pt.isel.ls.tests.utils.createUser
import pt.isel.ls.tests.utils.dataMem
import pt.isel.ls.tests.utils.dataSetup
import pt.isel.ls.tests.utils.dummyBoardDescription
import pt.isel.ls.tests.utils.dummyBoardName
import pt.isel.ls.tests.utils.executorTest
import pt.isel.ls.tests.utils.invalidToken
import pt.isel.ls.tests.utils.services
import pt.isel.ls.tests.utils.user
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class BoardAPITests {

    @BeforeTest
    fun setup() {
        dataSetup(Board::class.java)
    }

    @Test
    fun `test create board while logged in`() {
        val boardIn = BoardIn(dummyBoardName, dummyBoardDescription)
        val body = Json.encodeToString(boardIn)

        val response = app(
            Request(
                Method.POST,
                "$baseUrl/board"
            ).header(
                "Authorization",
                "Bearer ${user.token}"
            ).body(
                body
            )
        )

        val board = Json.decodeFromString<BoardOut>(response.bodyString())

        assertEquals(Status.CREATED, response.status)
        assertEquals(1, board.idBoard)
    }

    @Test
    fun `test create board without being logged`() {
        val boardIn = Json.encodeToString(BoardIn(dummyBoardName, dummyBoardDescription))

        val response = app(
            Request(Method.POST, "$baseUrl/board")
                .body(boardIn)
        )

        val msg = Json.decodeFromString<String>(response.bodyString())

        assertTrue(boards.isEmpty())
        assertEquals(Status.UNAUTHORIZED, response.status)
        assertEquals("Unauthorized Operation.", msg)
    }

    @Test
    fun `test create board without a body`() {
        val response = app(
            Request(Method.POST, "$baseUrl/board")
                .header("Authorization", "Bearer ${user.token}")
        )

        assertTrue(boards.isEmpty())
        assertEquals(Status.BAD_REQUEST, response.status)
    }

    @Test
    fun `test get all boards while logged in`() {
        val boardsAmount = 3
        repeat(boardsAmount) { id ->
            executorTest.execute {
                val idBoard = dataMem.boardData.createBoard(user.idUser, dummyBoardName + id, dummyBoardDescription, it)
                dataMem.userBoardData.addUserToBoard(user.idUser, idBoard, it)
            }
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

        val fetchedBoards = Json.decodeFromString<TotalBoards>(response.bodyString())

        assertEquals(boardsAmount, fetchedBoards.totalBoards)
        fetchedBoards.boards.forEachIndexed { i, it ->
            assertTrue(usersBoards.any { it.idUser == user.idUser })
            assertEquals(i + 1, it.idBoard)
            assertEquals(dummyBoardName + i, it.name)
        }
        assertEquals(response.status, Status.OK)
    }

    @Test
    fun `test get all boards without being logged in`() {
        repeat(3) {
            executorTest.execute { con ->
                dataMem.boardData.createBoard(user.idUser, dummyBoardName + it, dummyBoardDescription, con)
            }
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

        val fetchedBoards = Json.decodeFromString<TotalBoards>(response.bodyString())

        assertTrue(fetchedBoards.boards.isEmpty())
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

        val board = Json.decodeFromString<BoardDetailed>(response.bodyString())

        assertEquals(boardId, board.idBoard)
        assertTrue(usersBoards.any { it.idUser == user.idUser })
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
        val userId2 = Json.encodeToString(EmailUser(dummyEmail2))

        val response = app(
            Request(
                Method.PUT,
                "$baseUrl/board/$boardId"
            ).header(
                "Authorization",
                user.token
            ).body(userId2)
        )

        assertEquals(usersBoards.last().idUser, user2.first)
        assertEquals(Status.OK, response.status)
    }

    @Test
    fun `test add user to board without being logged in`() {
        val boardId = createBoard(user.idUser)

        val dummyName2 = "Diogo"
        val dummyEmail2 = "Diogo@gmail.com"
        val user2 = createUser(dummyName2, dummyEmail2)
        val userId2 = Json.encodeToString(EmailUser(dummyEmail2))

        val response = app(
            Request(Method.PUT, "$baseUrl/board/$boardId")
                .header("Authorization", invalidToken)
                .body(userId2)
        )

        val msg = Json.decodeFromString<String>(response.bodyString())

        assertNotEquals(usersBoards.last().idUser, user2.first)
        assertEquals(Status.UNAUTHORIZED, response.status)
        assertEquals("Unauthorized Operation.", msg)
    }

    @Test
    fun `get users from board`() {
        val user1 = createUser("Boris", "boris@gmail.com")
        val user2 = createUser("Johson", "Johson@gmail.com")
        val user3 = createUser("Pelegrini", "Pelegrini@gmail.com")

        val boardId = createBoard(user.idUser)
        executorTest.execute {
            dataMem.userBoardData.addUserToBoard(user1.first, boardId, it)
            dataMem.userBoardData.addUserToBoard(user2.first, boardId, it)
            dataMem.userBoardData.addUserToBoard(user3.first, boardId, it)
        }

        val response = app(
            Request(Method.GET, "$baseUrl/board/$boardId/allUsers")
                .header("Authorization", user.token)
        )

        val fetchedLists = Json.decodeFromString<List<User>>(response.bodyString())

        assertEquals(4, fetchedLists.size)
        assertEquals(response.status, Status.OK)
    }

    @Test
    fun `get users from board without being logged in`() {
        val response = app(
            Request(Method.GET, "$baseUrl/board/$boardId/allUsers")
                .header("Authorization", invalidToken)
        )

        val msg = Json.decodeFromString<String>(response.bodyString())

        assertEquals(Status.UNAUTHORIZED, response.status)
        assertEquals("Unauthorized Operation.", msg)
    }

    @Test
    fun `get users from invalid board`() {
        val response = app(
            Request(Method.GET, "$baseUrl/board/0/allUsers")
                .header("Authorization", user.token)
        )

        val msg = Json.decodeFromString<String>(response.bodyString())

        assertEquals(Status.NOT_FOUND, response.status)
        assertEquals("Board not found.", msg)
    }

    @Test
    fun `get boards from user with valid pagination`() {
        val skip = 2
        val limit = 3
        val nrBoards = 6

        repeat(nrBoards) {
            services.boardServices.createBoard(user.token, "board$it", "description$it")
        }

        val response = app(
            Request(Method.GET, "$baseUrl/board?skip=$skip&limit=$limit")
                .header("Authorization", user.token)
        )

        val fetchedBoards = Json.decodeFromString<TotalBoards>(response.bodyString())

        println(fetchedBoards)

        assertEquals(limit, fetchedBoards.boards.size)
        assertEquals(nrBoards, fetchedBoards.totalBoards)
    }

    @Test
    fun `get boards from user with invalid pagination negative numbers`() {
        val skip = -2
        val limit = -2

        repeat(6) {
            services.boardServices.createBoard(user.token, "board$it", "description$it")
        }

        val response = app(
            Request(Method.GET, "$baseUrl/board?skip=$skip&limit=$limit")
                .header("Authorization", user.token)
        )

        val fetchedBoards = Json.decodeFromString<TotalBoards>(response.bodyString())

        assertEquals(boards.size, fetchedBoards.boards.subList(0, fetchedBoards.boards.size).size)
    }

    @Test
    fun `get boards from user with invalid pagination bigger than size`() {
        val limit = 10

        repeat(6) {
            services.boardServices.createBoard(user.token, "board$it", "description$it")
        }

        val response = app(
            Request(Method.GET, "$baseUrl/board?limit=$limit")
                .header("Authorization", user.token)
        )

        val fetchedBoards = Json.decodeFromString<TotalBoards>(response.bodyString())

        assertEquals(boards.size, fetchedBoards.boards.size)
    }

    @Test
    fun `get users from boards with valid pagination`() {
        val idBoard = services.boardServices.createBoard(user.token, "board1", "description1")

        repeat(6) {
            val newEmail = "$it@gmail.com"
            services.userServices.createUser("user$it", newEmail, "pass", null)
            services.boardServices.addUserToBoard(user.token, newEmail, idBoard)
        }

        val response = app(
            Request(Method.GET, "$baseUrl/board/$idBoard/allUsers")
                .header("Authorization", user.token)
        )

        val fetchedUsers = Json.decodeFromString<List<User>>(response.bodyString())

        assertEquals(users, fetchedUsers)
    }

    @Test
    fun `get users from board with invalid pagination negative numbers`() {
        val skip = -2
        val limit = -2

        val idBoard = services.boardServices.createBoard(user.token, "board1", "description1")

        repeat(6) {
            val newEmail = "$it@gmail.com"
            services.userServices.createUser("user$it", newEmail, "pass", null)
            services.boardServices.addUserToBoard(user.token, newEmail, idBoard)
        }

        val response = app(
            Request(Method.GET, "$baseUrl/board/$idBoard/allUsers?skip=$skip&limit=$limit")
                .header("Authorization", user.token)
        )

        val users = Json.decodeFromString<List<User>>(response.bodyString())

        assertEquals(users, users.subList(0, users.size))
    }

    @Test
    fun `get users from board with invalid pagination bigger than size`() {
        val skip = 10
        val limit = 7

        val idBoard = services.boardServices.createBoard(user.token, "board1", "description1")

        repeat(6) {
            val newEmail = "$it@gmail.com"
            services.userServices.createUser("user$it", newEmail, "pass", null)
            services.boardServices.addUserToBoard(user.token, newEmail, idBoard)
        }

        val response = app(
            Request(Method.GET, "$baseUrl/board/$idBoard/allUsers?skip=$skip&limit=$limit")
                .header("Authorization", user.token)
        )

        val users = Json.decodeFromString<List<User>>(response.bodyString())

        assertEquals(users, users.subList(0, users.size))
    }
}
