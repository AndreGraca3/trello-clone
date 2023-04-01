package pt.isel.ls.tests.services

import kotlin.test.BeforeTest
import kotlin.test.Test
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.BoardList
import pt.isel.ls.tests.utils.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ServicesListTests {

    @BeforeTest
    fun setup() {
        dataSetup(BoardList::class.java)
    }


    @Test
    fun `Create a List in board`() {
        val newBoardListId = services.listServices.createList(user.token, boardId, dummyBoardListName)
        assertEquals(0, newBoardListId)
    }

    @Test
    fun `Create a List in board with invalid token`() {
        val err = assertFailsWith<TrelloException.NotAuthorized> {
            services.listServices.createList(invalidToken, boardId, dummyBoardListName)
        }
        assertEquals(401, err.status.code)
        assertEquals("Unauthorized Operation.", err.message)
    }

    @Test
    fun `Get List from Board`() {
        val newBoardListId = createList(boardId)
        val boardList = services.listServices.getList(user.token, boardId, newBoardListId)
        assertEquals(newBoardListId, boardList.idList)
        assertEquals(boardId, boardList.idBoard)
    }

    @Test
    fun `Create List in invalid Board`() {
        val err = assertFailsWith<TrelloException.NotFound> {
            services.listServices.createList(user.token, invalidId, dummyBoardListName)
        }
        assertEquals(404, err.status.code)
        assertEquals("Board not found.", err.message)
    }

    @Test
    fun `Get non-existing List from Board`() {
        createList(boardId)
        val err = assertFailsWith<TrelloException.NotFound> {
            services.listServices.getList(user.token, boardId,invalidId)
        }
        assertEquals(404, err.status.code)
        assertEquals("BoardList not found.", err.message)
    }

    @Test
    fun `Get Lists of Board`() {
        val listsAmount = 2
        repeat(listsAmount) { createList(boardId, dummyBoardListName + it) }
        val lists = services.listServices.getListsOfBoard(user.token, boardId)
        repeat(listsAmount) {
        assertEquals(lists[it], services.listServices.getList(user.token, boardId, it))
        }
    }

    @Test
    fun `Get Lists of Board invalid token`() {
        val err = assertFailsWith<TrelloException.NotAuthorized> {
            services.listServices.createList(user.token, boardId, dummyBoardListName)
            services.listServices.createList(user.token, boardId, "List2")
            services.listServices.getListsOfBoard(invalidToken, boardId)
        }
        assertEquals(401, err.status.code)
        assertEquals("Unauthorized Operation.", err.message)
    }
}