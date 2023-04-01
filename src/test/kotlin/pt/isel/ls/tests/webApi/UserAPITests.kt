package pt.isel.ls.tests.webApi

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.*
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import pt.isel.ls.server.utils.User
import pt.isel.ls.server.utils.UserIn
import pt.isel.ls.server.utils.UserOut
import pt.isel.ls.tests.utils.*

class UserAPITests {

    @BeforeTest
    fun setup() {
        dataSetup(User::class.java)
    }


    @Test
    fun `test createUser`() {
        val userIn = UserIn("User1", "user1@gmail.com")
        val requestBody = Json.encodeToString(userIn)

        val response = app(Request(Method.POST, "$baseUrl/user").body(requestBody))
        val userOut = Json.decodeFromString<UserOut>(response.bodyString())

        assertEquals(Status.CREATED, response.status)
        assertEquals(response.header("content-type"), "application/json")
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
        createUser()
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

        assertEquals(Status.BAD_REQUEST,response.status)
    }

    @Test
    fun `test get user user while being logged`() {
            val user = createUser()

            val response = app(
                Request(
                    Method.GET,
                    "$baseUrl/user"
                ).header("Authorization", "Bearer ${user.second}")
            )
            val userOut = Json.decodeFromString<User>(response.bodyString())

            assertEquals(Status.OK, response.status)
            assertEquals(user.first, userOut.idUser)
            assertEquals(dummyName, userOut.name)
            assertEquals(dummyEmail, userOut.email)
            assertEquals(user.second, userOut.token)
    }

    @Test
    fun `test get user without being logged in`() {
        createUser()

        val response = app(
            Request(Method.GET,"$baseUrl/user"))

        val msg = Json.decodeFromString<String>(response.bodyString())

        assertEquals(Status.UNAUTHORIZED,response.status)
        assertEquals("Unauthorized Operation.",msg)
    }
}