package pt.isel.ls.tests.services

import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.Board
import pt.isel.ls.tests.utils.createBoard
import pt.isel.ls.tests.utils.createUser
import pt.isel.ls.tests.utils.dataMem
import pt.isel.ls.tests.utils.dataSetup
import pt.isel.ls.tests.utils.dummyBoardDescription
import pt.isel.ls.tests.utils.dummyBoardName
import pt.isel.ls.tests.utils.dummyEmail
import pt.isel.ls.tests.utils.dummyName
import pt.isel.ls.tests.utils.invalidId
import pt.isel.ls.tests.utils.invalidToken
import pt.isel.ls.tests.utils.services
import pt.isel.ls.tests.utils.user
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ServicesBoardTests {

    @BeforeTest
    fun setup() {
        dataSetup(Board::class.java)
    }

    @Test
    fun `Create a valid board`() {
        val newBoardId = services.boardServices.createBoard(user.token, dummyBoardName, dummyBoardDescription)
        assertEquals(1, newBoardId)
    }

    @Test
    fun `Create board with invalid token`() {
        val err = assertFailsWith<TrelloException.NotAuthorized> {
            services.boardServices.createBoard(invalidToken, dummyBoardName, dummyBoardDescription)
        }
        assertEquals(401, err.status.code)
        assertEquals("Unauthorized Operation.", err.message)
    }

    @Test
    fun `Create board with invalid name`() {
        val err = assertFailsWith<TrelloException.AlreadyExists> {
            services.boardServices.createBoard(user.token, dummyBoardName, dummyBoardDescription)
            services.boardServices.createBoard(user.token, dummyBoardName, "This is Board2")
        }
        assertEquals(409, err.status.code)
        assertEquals("Board Board1 already exists.", err.message)
    }

    @Test
    fun `Get Board with Invalid Token`() {
        val err = assertFailsWith<TrelloException.NotAuthorized> {
            val newBoardId = createBoard(user.idUser)
            services.boardServices.getBoard(invalidToken, newBoardId)
        }
        assertEquals(401, err.status.code)
        assertEquals("Unauthorized Operation.", err.message)
    }

    @Test
    fun `Get invalid Board`() {
        val err = assertFailsWith<TrelloException.NotFound> {
            services.boardServices.getBoard(user.token, invalidId)
        }
        assertEquals(404, err.status.code)
        assertEquals("Board not found.", err.message)
    }

    @Test
    fun `Add User to a Board`() {
        val user2 = createUser(dummyName + 2, dummyEmail + 2)
        val newBoardId = createBoard(user.idUser)
        services.boardServices.addUserToBoard(user.token, user2.first, newBoardId)
        val board = services.boardServices.getBoard(user.token, newBoardId)
        val usersBoards = dataMem.userBoardData.usersBoards
        assertEquals(usersBoards.first().idUser, user.idUser)
        assertEquals(usersBoards.last().idUser, user2.first)
        assertEquals(newBoardId, board.idBoard)
        assertEquals(usersBoards.first().idBoard, board.idBoard)
        assertEquals(usersBoards.last().idBoard, board.idBoard)
    }

    @Test
    fun `Add User to a Board with Invalid Token`() {
        val err = assertFailsWith<TrelloException.NotAuthorized> {
            val user2 = createUser(dummyName + 2, dummyEmail + 2)
            val newBoardId = createBoard(user.idUser)
            services.boardServices.addUserToBoard(invalidToken, user2.first, newBoardId)
        }
        assertEquals(401, err.status.code)
        assertEquals("Unauthorized Operation.", err.message)
    }

    @Test
    fun `Add invalid User to a Board`() {
        val err = assertFailsWith<TrelloException.NotFound> {
            val newBoardId = createBoard(user.idUser)
            services.boardServices.addUserToBoard(user.token, invalidId, newBoardId)
        }
        assertEquals(404, err.status.code)
        assertEquals("User not found.", err.message)
    }

    @Test
    fun `Add User to invalid Board`() {
        val err = assertFailsWith<TrelloException.NotFound> {
            val user2 = createUser(dummyName + 2, dummyEmail + 2)
            services.boardServices.addUserToBoard(user.token, user2.first, invalidId)
        }
        assertEquals(404, err.status.code)
        assertEquals("Board not found.", err.message)
    }

    @Test
    fun `Get Users from Board`() {
        val user2 = createUser(dummyName + 2, dummyEmail + 2)
        val newBoardId = createBoard(user.idUser)
        services.boardServices.addUserToBoard(user.token, user2.first, newBoardId)
        val users = services.boardServices.getUsersFromBoard(user.token, newBoardId, null, null)
        assertEquals(2, users.size)
        assertEquals(user.idUser, users.first().idUser)
        assertEquals(user2.first, users.last().idUser)
    }

    @Test
    fun `Get Users from Board with Invalid Token`() {
        val err = assertFailsWith<TrelloException.NotAuthorized> {
            val user2 = createUser(dummyName + 2, dummyEmail + 2)
            val newBoardId = createBoard(user.idUser)
            services.boardServices.addUserToBoard(user.token, user2.first, newBoardId)
            services.boardServices.getUsersFromBoard(invalidToken, newBoardId, null, null)
        }
        assertEquals(401, err.status.code)
        assertEquals("Unauthorized Operation.", err.message)
    }

    @Test
    fun `Get Users from invalid Board`() {
        val err = assertFailsWith<TrelloException.NotFound> {
            services.boardServices.getUsersFromBoard(user.token, invalidId, null, null)
        }
        assertEquals(404, err.status.code)
        assertEquals("Board not found.", err.message)
    }

    @Test
    fun `Get Boards from User`() {
        val user2 = createUser(dummyName + 2, dummyEmail + 2)
        val newBoardId = createBoard(user.idUser)
        services.boardServices.addUserToBoard(user.token, user2.first, newBoardId)
        val boards = services.boardServices.getBoardsFromUser(user.token, null, null)
        assertEquals(1, boards.size)
        assertEquals(newBoardId, boards.first().idBoard)
    }
}
