package pt.isel.ls.tests.services

import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.BoardList
import pt.isel.ls.tests.utils.boardId
import pt.isel.ls.tests.utils.createList
import pt.isel.ls.tests.utils.dataSetup
import pt.isel.ls.tests.utils.dummyBoardListName
import pt.isel.ls.tests.utils.invalidId
import pt.isel.ls.tests.utils.invalidToken
import pt.isel.ls.tests.utils.services
import pt.isel.ls.tests.utils.user
import kotlin.test.BeforeTest
import kotlin.test.Test
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
        assertEquals(1, newBoardListId)
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
            services.listServices.getList(user.token, boardId, invalidId)
        }
        assertEquals(404, err.status.code)
        assertEquals("List not found.", err.message)
    }

    @Test
    fun `Get Lists of Board`() {
        val listsAmount = 2
        repeat(listsAmount) { createList(boardId, dummyBoardListName + it) }
        val lists = services.listServices.getListsOfBoard(user.token, boardId)
        repeat(listsAmount) {
            assertEquals(lists[it], services.listServices.getList(user.token, boardId, it + 1))
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

    @Test
    fun `Delete List from Board`() {
        val listId = createList(boardId)
        services.listServices.deleteList(user.token, boardId, listId, null)
        assertFailsWith<TrelloException.NotFound> {
            services.listServices.getList(user.token, boardId, listId)
        }
    }

    @Test
    fun `Delete List from Board invalid token`() {
        val listId = createList(boardId)
        val err = assertFailsWith<TrelloException.NotAuthorized> {
            services.listServices.deleteList(invalidToken, boardId, listId, null)
        }
        assertEquals(401, err.status.code)
        assertEquals("Unauthorized Operation.", err.message)
    }

    @Test
    fun `Delete non-existing List from Board`() {
        val err = assertFailsWith<TrelloException.NoContent> {
            services.listServices.deleteList(user.token, boardId, invalidId, null)
        }
        assertEquals(204, err.status.code)
        assertEquals("", err.message)
    }

    // IDEA: Update List
//    @Test
//    fun `Update List from Board`() {
//        val listId = createList(boardId)
//        val newName = "NewName"
//        services.listServices.updateList(user.token, boardId, listId, newName)
//        val list = services.listServices.getList(user.token, boardId, listId)
//        assertEquals(newName, list.name)
//    }
//
//    @Test
//    fun `Update List from Board invalid token`() {
//        val listId = createList(boardId)
//        val newName = "NewName"
//        val err = assertFailsWith<TrelloException.NotAuthorized> {
//            services.listServices.updateList(invalidToken, boardId, listId, newName)
//        }
//        assertEquals(401, err.status.code)
//        assertEquals("Unauthorized Operation.", err.message)
//    }
}
