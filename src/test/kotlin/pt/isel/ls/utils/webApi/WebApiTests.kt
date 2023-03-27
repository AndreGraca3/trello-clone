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
import pt.isel.ls.server.data.cardData.DataCard
import pt.isel.ls.server.data.initialState
import pt.isel.ls.server.data.listData.DataList
import pt.isel.ls.server.data.userData.DataUser
import pt.isel.ls.utils.*

class WebApiTest {

    //private val httpClient: HttpHandler = JavaHttpClient()

    private val baseUrl = "http://localhost:8080"

    private val validToken = "b2751dfa-1386-440a-bb9b-0927f6ad6163"

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
        "board/{idBoard}/{idUser}" bind Method.PUT to webApi::addUserToBoard, /** verificar este path com o martin ( path ou body)**/
        "board/{idBoard}" bind Method.GET to webApi::getBoard,
        "board/{idBoard}/list" bind Method.POST to webApi::createList,
        "board/{idBoard}/list" bind Method.GET to webApi::getListsFromBoard,
        "board/{idBoard}/list/{idList}" bind Method.GET to webApi::getList, /** requer idBody? **/
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
