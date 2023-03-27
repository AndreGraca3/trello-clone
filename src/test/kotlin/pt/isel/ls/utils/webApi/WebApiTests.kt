package pt.isel.ls.utils.webApi

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.*
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.routing.bind
import org.http4k.routing.routes
import pt.isel.ls.*
import pt.isel.ls.server.Services
import pt.isel.ls.server.WebApi
import pt.isel.ls.server.data.boardData.DataBoard
import pt.isel.ls.server.data.boards
import pt.isel.ls.server.data.cardData.DataCard
import pt.isel.ls.server.data.initialState
import pt.isel.ls.server.data.listData.DataList
import pt.isel.ls.server.data.userData.DataUser
import pt.isel.ls.utils.*

class WebApiTest {

    //private val httpClient: HttpHandler = JavaHttpClient()

    private val baseUrl = "http://localhost:8080"


    private val dataUser = DataUser()
    private val dataBoard = DataBoard()
    private val dataList = DataList()
    private val dataCard = DataCard()
    private val webApi = WebApi(Services(dataUser, dataBoard, dataList, dataCard))

    /** Every Test Starts with **/
    @BeforeTest
    fun dataSetup() {
        initialState()
    }

    /*@BeforeClass
    fun severSetup() {
        val testRoutes = routes(
            "user" bind Method.POST to webApi::createUser,
            /** add all routes.**/
        )

        jettyServer = testRoutes.asServer(Jetty(8080)).start()
    }*/

    private val app = routes(
        "user" bind Method.POST to webApi::createUser,
        "user/{idUser}" bind Method.GET to webApi::getUser,
        "board" bind Method.POST to webApi::createBoard,
        "board" bind Method.GET to webApi::getBoardsFromUser,
        "board/{idBoard}" bind Method.PUT to webApi::addUserToBoard,
        "board/{idBoard}" bind Method.GET to webApi::getBoard,
        "board/{idBoard}/list" bind Method.POST to webApi::createList,
        "board/{idBoard}/list" bind Method.GET to webApi::getListsFromBoard,
        "board/{idBoard}/list/{idList}" bind Method.GET to webApi::getList,
        "board/{idBoard}/list/{idList}/card" bind Method.POST to webApi::createCard,
        "board/{idBoard}/list/{idList}/card" bind Method.GET to webApi::getCardsFromList,
        "board/{idBoard}/list/{idList}/card/{idCard}" bind Method.GET to webApi::getCard,
        "board/{idBoard}/list/{idList}/card/{idCard}" bind Method.PUT to webApi::moveCard /** idList destination comes in body.**/
    )

    @Test
    fun `test createUser`() {
        val userIn = UserIn("User1", "user1@gmail.com")
        val requestBody = Json.encodeToString(userIn)
        val response = app(Request(Method.POST, "$baseUrl/user").body(requestBody))

        assertEquals(Status.CREATED, response.status)
        assertEquals(response.header("content-type"), "application/json")

        val userOut = Json.decodeFromString<UserOut>(response.bodyString())
        assertEquals(0, userOut.idUser)
    }

    @Test
    fun `test create and getUser`() {
        val userIn = dataUser.createUser(dummyName, dummyEmail)

        val response = app(Request(Method.GET, "$baseUrl/user/${userIn.first}")
            .header("Authorization", "Bearer ${userIn.second}"))

        assertEquals(Status.OK, response.status)

        val userOut = Json.decodeFromString<User>(response.bodyString())

        assertEquals(userIn.first,userOut.idUser)
        assertEquals(dummyName, userOut.name)
        assertEquals(dummyEmail,userOut.email)
        assertEquals(userIn.second,userOut.token)
    }

    @Test
    fun `create list`() {
        val user = dataUser.createUser(dummyName, dummyEmail)
        val idBoard = dataBoard.createBoard(user.first, dummyBoardName, dummyBoardDescription)

        val list = BoardListIn(dummyBoardListName)

        val response = app(Request(Method.POST, "$baseUrl/board/$idBoard/list")
            .body(Json.encodeToString(list))
            .header("Authorization", "Bearer ${user.second}")
        )

        val listOut = Json.decodeFromString<Int>(response.bodyString())

        assertEquals(0, listOut)
    }

    @Test
    fun `create and get list`() {
        val user = dataUser.createUser(dummyName, dummyEmail)
        val idBoard = dataBoard.createBoard(user.first, dummyBoardName, dummyBoardDescription)
        val idList = dataList.createList(idBoard, dummyBoardListName)

        val response = app(Request(Method.GET, "$baseUrl/board/$idBoard/list/$idList")
            .header("Authorization", "Bearer ${user.second}")
        )

        val listOut = Json.decodeFromString<BoardList>(response.bodyString())

        assertEquals(0, listOut.idList)
        assertEquals(dummyBoardListName, listOut.name)
        assertEquals(idBoard, listOut.idBoard)
    }

    @Test
    fun `test create board`() {
        val token = dataUser.createUser(dummyName, dummyEmail).second

        val boardIn = Json.encodeToString(BoardIn(dummyBoardName, dummyBoardDescription))

        val response = app(Request(Method.POST,"$baseUrl/board")
            .header("Authorization","Bearer $token")
            .body(boardIn))

        assertEquals(Status.CREATED,response.status)

        val board = Json.decodeFromString<BoardOut>(response.bodyString())

        assertEquals(0,board.idBoard)
    }

    @Test
    fun `test get all boards`() {
        val user = dataUser.createUser(dummyName, dummyEmail)
        val names = listOf("board1","board2","board3")
        for(i in 0..2)  {
            dataBoard.createBoard(user.first,names[i], dummyBoardDescription)
        }

        val boardData = boards

        val response = app(Request(Method.GET,"$baseUrl/board")
            .header("Authorization",user.second))

        assertEquals(response.status,Status.OK)

        val boards = Json.decodeFromString<List<Board>>(response.bodyString())

        assertEquals(boards,boardData)
    }

    @Test
    fun `test add a user to a board`() {
        val user1 = dataUser.createUser(dummyName, dummyEmail)
        val boardId = dataBoard.createBoard(user1.first, dummyBoardName, dummyBoardDescription)

        val boardsData = boards

        val dummyName2 = "Diogo"
        val dummyEmail2 = "Diogo@gmail.com"
        val user2 = dataUser.createUser(dummyName2,dummyEmail2)
        val userId2 = Json.encodeToString(user2.first)

        val response = app(Request(Method.PUT,"$baseUrl/board/$boardId")
            .header("Authorization",user1.second)
            .body(userId2))

        assertEquals(Status.OK,response.status)

        assertTrue(boardsData.first().idUsers.contains(user2.first))
    }

    @Test
    fun `test get details of board`() {
        val user = dataUser.createUser(dummyName, dummyEmail)
        val boardId = dataBoard.createBoard(user.first, dummyBoardName, dummyBoardDescription)

        val response = app(Request(Method.GET,"$baseUrl/board/$boardId")
            .header("Authorization",user.second))

        assertEquals(Status.OK,response.status)

        val board = Json.decodeFromString<Board>(response.bodyString())

        assertEquals(
            Board(boardId, dummyBoardName, dummyBoardDescription, mutableListOf(user.first)),
            board)

    }

    /*@AfterClass
    fun serverStop(){
        jettyServer.stop()
    }*/

    @Test
    fun `create card without endDate`(){
        val userIn = dataUser.createUser(dummyName, dummyEmail)
        val idBoard = dataBoard.createBoard(userIn.first, dummyBoardName, dummyBoardDescription)
        val idList = dataList.createList(idBoard, dummyBoardListName)
        val cardIn = CardIn(dummyCardName, dummyCardDescription, null)

        val requestBody = Json.encodeToString(cardIn)

        val response = app(Request(Method.POST, "$baseUrl/board/${idBoard}/list/${idList}/card")
            .header("Authorization", "Bearer ${userIn.second}")
            .body(requestBody))

        assertEquals(Status.CREATED, response.status)
        assertEquals("application/json",response.header("content-type"))

        val bodyString = response.bodyString()

        val cardOut = Json.decodeFromString<Int>(bodyString)
        assertEquals(0, cardOut)
    }

    @Test
    fun `get card`(){
        val userIn = dataUser.createUser(dummyName, dummyEmail)
        val idBoard = dataBoard.createBoard(userIn.first, dummyBoardName, dummyBoardDescription)
        val idList = dataList.createList(idBoard, dummyBoardListName)
        val idCard = dataCard.createCard(idList, dummyCardName, dummyCardDescription, null)

        val response = app(Request(Method.GET, "$baseUrl/board/${idBoard}/list/${idList}/card/${idCard}")
            .header("Authorization", "Bearer ${userIn.second}"))

        assertEquals(Status.OK, response.status)
        assertEquals("application/json", response.header("content-type"))

        val cardOut = Json.decodeFromString<CardOut>(response.bodyString())
        assertEquals(dummyCardName, cardOut.name)
        assertEquals(dummyCardDescription, cardOut.description)
    }

    @Test
    fun `move card`(){
        val userIn = dataUser.createUser(dummyName, dummyEmail)
        val idBoard = dataBoard.createBoard(userIn.first, dummyBoardName, dummyBoardDescription)
        val idList = dataList.createList(idBoard, dummyBoardListName)
        val idCard = dataCard.createCard(idList, dummyCardName, dummyCardDescription, null)
        val idListDst = dataList.createList(idBoard, dummyBoardListName)

        val requestBody = Json.encodeToString(idListDst)

        val response = app(Request(Method.PUT, "$baseUrl/board/${idBoard}/list/${idList}/card/${idCard}")
            .header("Authorization", "Bearer ${userIn.second}")
            .body(requestBody))

        assertEquals(Status.OK, response.status)
        assertEquals( "application/json",response.header("content-type"))

    }
    @Test
    fun `get all lists from board`() {
        val user = dataUser.createUser(dummyName, dummyEmail)
        val idBoard = dataBoard.createBoard(user.first, dummyBoardName, dummyBoardDescription)
        repeat(3) {dataList.createList(idBoard, dummyBoardListName)}

        val response = app(Request(Method.GET, "$baseUrl/board/$idBoard/list")
            .header("Authorization", "Bearer ${user.second}")
        )

        val listsOut = Json.decodeFromString<List<BoardList>>(response.bodyString())

        val it = listsOut.iterator()

        while (it.hasNext()) {
            val elem = it.next()
            assertEquals(idBoard, elem.idBoard)
        }
    }
}
