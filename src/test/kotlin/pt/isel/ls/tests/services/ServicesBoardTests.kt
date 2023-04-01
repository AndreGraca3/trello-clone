package pt.isel.ls.tests.services

import kotlin.test.BeforeTest
import kotlin.test.Test
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.Board
import pt.isel.ls.tests.utils.*
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
        assertEquals(0, newBoardId)
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
    fun `Add User to a Board`() {
        val user2 = createUser(dummyName+2, dummyEmail+2)
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
            val user2 = createUser(dummyName+2, dummyEmail+2)
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
}