package pt.isel.ls.utils.webApi

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.*
import org.http4k.*
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import pt.isel.ls.UserIn
import pt.isel.ls.UserOut
import pt.isel.ls.User
import org.http4k.client.JavaHttpClient
import org.http4k.core.*
import org.http4k.metrics.MetricsDefaults.Companion.client
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Jetty
import org.http4k.server.asServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import pt.isel.ls.server.Services
import pt.isel.ls.server.WebApi
import pt.isel.ls.server.data.boardData.DataBoard
import pt.isel.ls.server.data.cardData.DataCard
import pt.isel.ls.server.data.listData.DataList
import pt.isel.ls.server.data.userData.DataUser

class WebApiTest {

    private val httpClient: HttpHandler = JavaHttpClient()

    private val baseUrl = "http://localhost:8080"

    private val validToken = "b2751dfa-1386-440a-bb9b-0927f6ad6163"

    private val webApi = WebApi(Services(DataUser(), DataBoard(), DataList(), DataCard()))

    @Test
    fun `test createUser`() {
        val userIn = UserIn("User1", "user1@gmail.com")
        val requestBody = Json.encodeToString(userIn)
        val request = Request(Method.POST, "$baseUrl/user")
            .header("Authorization", "Bearer $validToken")
            .header("content-type", "application/json")
            .body(requestBody)

        val testRoutes = routes(
            "user" bind Method.POST to webApi::createUser,
        )
        val jettyServer = testRoutes.asServer(Jetty(8080)).start()

        val response = httpClient(request)

        assertEquals(Status.OK, response.status)
        val userOut = Json.decodeFromString<UserOut>(response.bodyString())
        assertEquals(0, userOut.idUser)

        jettyServer.stop()
    }

    @Test
    fun `test create and getUser`() {
        val request = Request(Method.GET, "$baseUrl/user/0")
            .header("Authorization", "Bearer abc123")

        val response = httpClient(request)

        assertEquals(Status.OK, response.status)
        val user = Json.decodeFromString<User>(response.bodyString())
        assertEquals("John", user.name)
        assertEquals("john@example.com", user.email)
    }
}
