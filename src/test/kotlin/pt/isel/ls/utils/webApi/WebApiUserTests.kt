package pt.isel.ls.utils.webApi

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.*
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import pt.isel.ls.*
import pt.isel.ls.server.data.initialState
import pt.isel.ls.utils.*

class WebApiUserTests {

    /** Every Test Starts with **/
    @BeforeTest
    fun dataSetup() {
        initialState()
    }

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
    fun `test create user with invalid mail`() {
        val userIn = UserIn(dummyName, dummyBadEmail)
        val requestBody = Json.encodeToString(userIn)
        val response = app(Request(Method.POST, "$baseUrl/user").body(requestBody))

        val msg = Json.decodeFromString<String>(response.bodyString())

        assertEquals(Status.BAD_REQUEST, response.status)
        assertEquals("Invalid parameters: $dummyBadEmail",msg)
    }

    @Test
    fun `test create already existing user`() {
        val userIn1 = dataUser.createUser(dummyName, dummyEmail)

        val userIn2 = UserIn(dummyName, dummyEmail)
        val requestBody = Json.encodeToString(userIn2)
        val response = app(Request(Method.POST,"$baseUrl/user").body(requestBody))

        val msg = Json.decodeFromString<String>(response.bodyString())

        assertEquals(Status.CONFLICT, response.status)
        assertEquals("$dummyEmail already exists.",msg)
    }

    @Test
    fun `test create user without body`() {
        val response = app(Request(Method.POST,"$baseUrl/user"))

        val msg = Json.decodeFromString<String>(response.bodyString())

        assertEquals(Status.BAD_REQUEST,response.status)
    }

    @Test
    fun `test get user user while being logged`() {
            val userIn = dataUser.createUser(dummyName, dummyEmail)

            val response = app(
                Request(
                    Method.GET,
                    "$baseUrl/user"
                ).header("Authorization", "Bearer ${userIn.second}")
            )

            assertEquals(Status.OK, response.status)

            val userOut = Json.decodeFromString<User>(response.bodyString())

            assertEquals(userIn.first, userOut.idUser)
            assertEquals(dummyName, userOut.name)
            assertEquals(dummyEmail, userOut.email)
            assertEquals(userIn.second, userOut.token)
    }

    @Test
    fun `test get user without being logged in`() {
        val user = dataUser.createUser(dummyName, dummyEmail)

        val response = app(
            Request(Method.GET,"$baseUrl/user")
            .header("Authorization","Bearer $invalidToken"))

        val msg = Json.decodeFromString<String>(response.bodyString())

        assertEquals(Status.UNAUTHORIZED,response.status)
        assertEquals("Unauthorized Operation.",msg)
    }
}