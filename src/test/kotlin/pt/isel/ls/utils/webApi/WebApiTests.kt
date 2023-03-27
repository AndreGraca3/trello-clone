package pt.isel.ls.utils.webApi

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.*
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import pt.isel.ls.UserIn
import pt.isel.ls.UserOut
import pt.isel.ls.User
import org.http4k.routing.bind
import org.http4k.routing.routes
import pt.isel.ls.server.Services
import pt.isel.ls.server.WebApi
import pt.isel.ls.server.data.boardData.DataBoard
import pt.isel.ls.server.data.cardData.DataCard
import pt.isel.ls.server.data.initialState
import pt.isel.ls.server.data.listData.DataList
import pt.isel.ls.server.data.userData.DataUser
import pt.isel.ls.utils.dummyEmail
import pt.isel.ls.utils.dummyName

class WebApiTest {

    //private val httpClient: HttpHandler = JavaHttpClient()

    private val baseUrl = "http://localhost:8080"

    private val validToken = "b2751dfa-1386-440a-bb9b-0927f6ad6163"

    private val dataUser = DataUser()

    private val dataBoard = DataBoard()

    private val dataList = DataList()

    private val dataCard = DataCard()

    private val webApi = WebApi(Services(dataUser, dataBoard, dataList, dataCard))



    //private lateinit var jettyServer : Http4kServer

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
        "user/{idUser}" bind Method.GET to webApi::getUser
        /** add all routes.**/
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

    /*@AfterClass
    fun serverStop(){
        jettyServer.stop()
    }*/
}
