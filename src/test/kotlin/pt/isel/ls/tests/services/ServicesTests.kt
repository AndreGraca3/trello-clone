//package pt.isel.ls.tests.services
//
//import pt.isel.ls.server.Services
//import pt.isel.ls.server.data.boardData.DataBoard
//import pt.isel.ls.server.data.cardData.DataCard
//import pt.isel.ls.server.data.initialState
//import pt.isel.ls.server.data.listData.DataList
//import pt.isel.ls.server.data.userData.DataUser
//import pt.isel.ls.server.exceptions.TrelloException
//import pt.isel.ls.tests.utils.dummyBadEmail
//import pt.isel.ls.tests.utils.dummyBoardDescription
//import pt.isel.ls.tests.utils.dummyBoardListName
//import pt.isel.ls.tests.utils.dummyBoardName
//import pt.isel.ls.tests.utils.dummyCardDescription
//import pt.isel.ls.tests.utils.dummyCardName
//import pt.isel.ls.tests.utils.dummyEmail
//import pt.isel.ls.tests.utils.dummyName
//import pt.isel.ls.tests.utils.invalidEndDate
//import pt.isel.ls.tests.utils.invalidToken
//import pt.isel.ls.tests.utils.validEndDate
//import java.time.format.DateTimeParseException
//import kotlin.test.BeforeTest
//import kotlin.test.Test
//import kotlin.test.assertEquals
//import kotlin.test.assertFailsWith
//import kotlin.test.assertTrue
//
//class ServicesTests {
//
//    // Creation of data and services objects
//    private val userDataMem = DataUser()
//    private val boardDataMem = DataBoard()
//    private val listDataMem = DataList()
//    private val cardDataMem = DataCard()
//    private val services = Services(userDataMem, boardDataMem, listDataMem, cardDataMem)
//
//    /**
//     * We delete all data from DataMem in order to have a known initial state of data.
//     */
//    @BeforeTest
//    fun setup() {
//        initialState()
//    }
//
//    /** ----------------------------
//     *  User Test
//     *  ------------------------------**/
//    @Test
//    fun `Create a valid user`() {
//        val user = services.createUser(dummyName, dummyEmail)
//        assertEquals(0, user.first)
//    }
//
//    @Test
//    fun `Create a user with invalid email`() {
//        val err = assertFailsWith<TrelloException.IllegalArgument> {
//            services.createUser(dummyName, dummyBadEmail)
//        }
//        assertEquals(400, err.status.code)
//        assertEquals("Invalid parameters: $dummyBadEmail", err.message)
//    }
//
//    @Test
//    fun `Get existing user`() {
//        services.createUser(dummyName, dummyEmail)
//        val user = services.getUser(0)
//        assertEquals(0, user.idUser)
//        assertEquals(dummyName, user.name)
//        assertEquals(dummyEmail, user.email)
//    }
//
//    /** ----------------------------
//     *  Board Test
//     *  ------------------------------**/
//    @Test
//    fun `Create a valid board`() {
//        val user = services.createUser(dummyName, dummyEmail)
//        val userToken = user.second
//        val newBoardId = services.createBoard(userToken, dummyBoardName, dummyBoardDescription)
//        assertEquals(0, newBoardId)
//    }
//
//    @Test
//    fun `Create board with invalid token`() {
//        val err = assertFailsWith<TrelloException.NotAuthorized> {
//            services.createBoard(invalidToken, dummyBoardName, dummyBoardDescription)
//        }
//        assertEquals(401, err.status.code)
//        assertEquals("Unauthorized Operation.", err.message)
//    }
//
//    @Test
//    fun `Create board with invalid name`() {
//        val err = assertFailsWith<TrelloException.AlreadyExists> {
//            val user = services.createUser(dummyName, dummyEmail)
//            val userToken = user.second
//            services.createBoard(userToken, dummyBoardName, dummyBoardDescription)
//            services.createBoard(userToken, dummyBoardName, "This is Board2")
//        }
//        assertEquals(409, err.status.code)
//        assertEquals("Board1 already exists.", err.message)
//    }
//
//    @Test
//    fun `Add User to a Board`() {
//        val user1 = services.createUser(dummyName, dummyEmail)
//        val user2 = services.createUser("user2", "user2@gmail.com")
//        val newBoardId = services.createBoard(user1.second, dummyBoardName, dummyBoardDescription)
//        services.addUserToBoard(user1.second, user2.first, newBoardId)
//        val board = services.getBoard(user1.second, newBoardId)
//        assertEquals(board.idUsers.first(), user1.first)
//        assertEquals(board.idUsers.last(), user2.first)
//    }
//
//    @Test
//    fun `Add User to a Board with Invalid Token`() {
//        val err = assertFailsWith<TrelloException.NotAuthorized> {
//            val user1 = services.createUser(dummyName, dummyEmail)
//            val user2 = services.createUser("user2", "user2@gmail.com")
//            val newBoardId = services.createBoard(user1.second, dummyBoardName, dummyBoardDescription)
//            services.addUserToBoard(invalidToken, user2.first, newBoardId)
//        }
//        assertEquals(401, err.status.code)
//        assertEquals("Unauthorized Operation.", err.message)
//    }
//
//    @Test
//    fun `Add invalid User to a Board`() {
//        val err = assertFailsWith<TrelloException.NotFound> {
//            val user1 = services.createUser(dummyName, dummyEmail)
//            val invalidId = 5
//            val newBoardId = services.createBoard(user1.second, dummyBoardName, dummyBoardDescription)
//            services.addUserToBoard(user1.second, invalidId, newBoardId)
//        }
//        assertEquals(404, err.status.code)
//        assertEquals("User not found.", err.message)
//    }
//
//    /** ----------------------------
//     *  BoardList Test
//     *  ------------------------------**/
//    @Test
//    fun `Create a List in board`() {
//        val user = services.createUser(dummyName, dummyEmail)
//        val newBoardId = services.createBoard(user.second, dummyBoardName, dummyBoardDescription)
//        val newBoardListId = services.createList(user.second, newBoardId, dummyBoardListName)
//        assertEquals(0, newBoardListId)
//    }
//
//    @Test
//    fun `Create a List in board with invalid token`() {
//        val err = assertFailsWith<TrelloException.NotAuthorized> {
//            val user = services.createUser(dummyName, dummyEmail)
//            val newBoardId = services.createBoard(user.second, dummyBoardName, dummyBoardDescription)
//            services.createList(invalidToken, newBoardId, dummyBoardListName)
//        }
//        assertEquals(401, err.status.code)
//        assertEquals("Unauthorized Operation.", err.message)
//    }
//
//    @Test
//    fun `Get List from Board`() {
//        val user = services.createUser(dummyName, dummyEmail)
//        val newBoardId = services.createBoard(user.second, dummyBoardName, dummyBoardDescription)
//        val newBoardListId = services.createList(user.second, newBoardId, dummyBoardListName)
//        val boardList = services.getList(user.second, 0)
//        assertEquals(newBoardListId, boardList.idList)
//        assertEquals(newBoardId, boardList.idBoard)
//    }
//
//    @Test
//    fun `Create List in invalid Board`() {
//        val err = assertFailsWith<TrelloException.NotFound> {
//            val user = services.createUser(dummyName, dummyEmail)
//            services.createBoard(user.second, dummyBoardName, dummyBoardDescription)
//            val invalidId = 5
//            services.createList(user.second, invalidId, dummyBoardListName)
//        }
//        assertEquals(404, err.status.code)
//        assertEquals("Board not found.", err.message)
//    }
//
//    @Test
//    fun `Get List from invalid Board`() {
//        val err = assertFailsWith<TrelloException.NotFound> {
//            val user = services.createUser(dummyName, dummyEmail)
//            val newBoardId = services.createBoard(user.second, dummyBoardName, dummyBoardDescription)
//            services.createList(user.second, newBoardId, dummyBoardListName)
//            val invalidId = 5
//            services.getList(user.second, invalidId)
//        }
//        assertEquals(404, err.status.code)
//        assertEquals("BoardList not found.", err.message)
//    }
//
//    @Test
//    fun `Get Lists of Board`() {
//        val user1 = services.createUser(dummyName, dummyEmail)
//        val newBoardId = services.createBoard(user1.second, dummyBoardName, dummyBoardDescription)
//        val listId1 = services.createList(user1.second, newBoardId, dummyBoardListName)
//        val listId2 = services.createList(user1.second, newBoardId, "List2")
//        val lists = services.getListsOfBoard(user1.second, newBoardId)
//        assertEquals(lists.first(), services.getList(user1.second, listId1))
//        assertEquals(lists.last(), services.getList(user1.second, listId2))
//    }
//
//    @Test
//    fun `Get Lists of Board invalid token`() {
//        val err = assertFailsWith<TrelloException.NotAuthorized> {
//            val user1 = services.createUser(dummyName, dummyEmail)
//            val newBoardId = services.createBoard(user1.second, dummyBoardName, dummyBoardDescription)
//            services.createList(user1.second, newBoardId, dummyBoardListName)
//            services.createList(user1.second, newBoardId, "List2")
//            services.getListsOfBoard(invalidToken, newBoardId)
//        }
//        assertEquals(401, err.status.code)
//        assertEquals("Unauthorized Operation.", err.message)
//    }
//
//    /** ----------------------------
//     *  Card Test
//     *  ------------------------------**/
//
//    @Test
//    fun `Create a Card in List without endDate`() {
//        val user = services.createUser(dummyName, dummyEmail)
//        val newBoardId = services.createBoard(user.second, dummyBoardName, dummyBoardDescription)
//        val newBoardListId = services.createList(user.second, newBoardId, dummyBoardListName)
//        val newCardId = services.createCard(user.second, newBoardListId, dummyCardName, dummyCardDescription, null)
//        assertEquals(0, newCardId)
//    }
//
//    @Test
//    fun `Create a Card in List with endDate`() {
//        val user = services.createUser(dummyName, dummyEmail)
//        val newBoardId = services.createBoard(user.second, dummyBoardName, dummyBoardDescription)
//        val newBoardListId = services.createList(user.second, newBoardId, dummyBoardListName)
//        val newCardId = services.createCard(
//            user.second,
//            newBoardListId,
//            dummyCardName,
//            dummyCardDescription,
//            validEndDate
//        )
//        assertEquals(0, newCardId)
//    }
//
//    @Test
//    fun `Create a Card in List with wrong format endDate`() {
//        assertFailsWith<DateTimeParseException> {
//            val user = services.createUser(dummyName, dummyEmail)
//            val newBoardId = services.createBoard(user.second, dummyBoardName, dummyBoardDescription)
//            val newBoardListId = services.createList(user.second, newBoardId, dummyBoardListName)
//            services.createCard(user.second, newBoardListId, dummyCardName, dummyCardDescription, invalidEndDate)
//        }
//    }
//
//    @Test
//    fun `Create a Card in List with early endDate`() {
//        val invalidDueDate = "2000-06-06"
//        val err = assertFailsWith<TrelloException.IllegalArgument> {
//            val user = services.createUser(dummyName, dummyEmail)
//            val newBoardId = services.createBoard(user.second, dummyBoardName, dummyBoardDescription)
//            val newBoardListId = services.createList(user.second, newBoardId, dummyBoardListName)
//            services.createCard(user.second, newBoardListId, dummyCardName, dummyCardDescription, invalidDueDate)
//        }
//        assertEquals(400, err.status.code)
//        assertEquals("Invalid parameters: $invalidDueDate", err.message)
//    }
//
//    @Test
//    fun `Get Card from List`() {
//        val user = services.createUser(dummyName, dummyEmail)
//        val newBoardId = services.createBoard(user.second, dummyBoardName, dummyBoardDescription)
//        val newBoardListId = services.createList(user.second, newBoardId, dummyBoardListName)
//        val cardId = services.createCard(user.second, newBoardListId, dummyCardName, dummyCardDescription, validEndDate)
//        val card = services.getCard(user.second, cardId)
//        assertEquals(cardId, card.idCard)
//    }
//
//    @Test
//    fun `Get Card from List with invalid token`() {
//        val err = assertFailsWith<TrelloException.NotAuthorized> {
//            val user = services.createUser(dummyName, dummyEmail)
//            val newBoardId = services.createBoard(user.second, dummyBoardName, dummyBoardDescription)
//            val newBoardListId = services.createList(user.second, newBoardId, dummyBoardListName)
//            val cardId =
//                services.createCard(user.second, newBoardListId, dummyCardName, dummyCardDescription, validEndDate)
//            services.getCard(invalidToken, cardId)
//        }
//        assertEquals(401, err.status.code)
//        assertEquals("Unauthorized Operation.", err.message)
//    }
//
//    @Test
//    fun `Get Cards from List`() {
//        val user = services.createUser(dummyName, dummyEmail)
//        val newBoardId = services.createBoard(user.second, dummyBoardName, dummyBoardDescription)
//        val newBoardListId = services.createList(user.second, newBoardId, dummyBoardListName)
//        val cardId1 =
//            services.createCard(user.second, newBoardListId, dummyCardName, dummyCardDescription, validEndDate)
//        val cardId2 =
//            services.createCard(user.second, newBoardListId, dummyCardName, dummyCardDescription, validEndDate)
//        val cardList = services.getCardsFromList(user.second, newBoardListId)
//        assertEquals(cardList.first().idCard, cardId1)
//        assertEquals(cardList.last().idCard, cardId2)
//    }
//
//    @Test
//    fun `Move Card from a List to another List`() {
//        val user = services.createUser(dummyName, dummyEmail)
//        val newBoardId = services.createBoard(user.second, dummyBoardName, dummyBoardDescription)
//        val newBoardListId1 = services.createList(user.second, newBoardId, dummyBoardListName)
//        val cardId =
//            services.createCard(user.second, newBoardListId1, dummyCardName, dummyCardDescription, validEndDate)
//        assertEquals(cardId, services.getCardsFromList(user.second, newBoardListId1).first().idCard)
//
//        val newBoardListId2 = services.createList(user.second, newBoardId, dummyBoardListName)
//        services.moveCard(user.second, cardId, newBoardListId2)
//        assertEquals(cardId, services.getCardsFromList(user.second, newBoardListId2).first().idCard)
//        val list1 = services.getCardsFromList(user.second, newBoardListId1)
//        assertTrue(list1.isEmpty())
//    }
//
//    @Test
//    fun `Move Card from a List to a non existence List`() {
//        val err = assertFailsWith<TrelloException.NotFound> {
//            val user = services.createUser(dummyName, dummyEmail)
//            val newBoardId = services.createBoard(user.second, dummyBoardName, dummyBoardDescription)
//            val newBoardListId1 = services.createList(user.second, newBoardId, dummyBoardListName)
//            val cardId =
//                services.createCard(user.second, newBoardListId1, dummyCardName, dummyCardDescription, validEndDate)
//
//            val invalidBoardListId = 5
//            services.moveCard(user.second, cardId, invalidBoardListId)
//        }
//        assertEquals(404, err.status.code)
//        assertEquals("BoardList not found.", err.message)
//    }
//
//    @Test
//    fun `Move Card from a List to another List with invalid token`() {
//        val err = assertFailsWith<TrelloException.NotAuthorized> {
//            val user = services.createUser(dummyName, dummyEmail)
//            val newBoardId = services.createBoard(user.second, dummyBoardName, dummyBoardDescription)
//            val newBoardListId1 = services.createList(user.second, newBoardId, dummyBoardListName)
//
//            val cardId =
//                services.createCard(user.second, newBoardListId1, dummyCardName, dummyCardDescription, validEndDate)
//
//            val newBoardListId2 = services.createList(user.second, newBoardId, dummyBoardListName)
//            services.moveCard(invalidToken, cardId, newBoardListId2)
//        }
//        assertEquals(401, err.status.code)
//        assertEquals("Unauthorized Operation.", err.message)
//    }
//}
