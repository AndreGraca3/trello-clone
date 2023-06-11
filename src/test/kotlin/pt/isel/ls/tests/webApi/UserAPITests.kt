package pt.isel.ls.tests.webApi

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import pt.isel.ls.server.User
import pt.isel.ls.server.UserIn
import pt.isel.ls.server.UserOut
import pt.isel.ls.tests.utils.app
import pt.isel.ls.tests.utils.baseUrl
import pt.isel.ls.tests.utils.createUser
import pt.isel.ls.tests.utils.dataSetup
import pt.isel.ls.tests.utils.dummyAvatar
import pt.isel.ls.tests.utils.dummyBadEmail
import pt.isel.ls.tests.utils.dummyEmail
import pt.isel.ls.tests.utils.dummyName
import pt.isel.ls.tests.utils.dummyPassword
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class UserAPITests {

    @BeforeTest
    fun setup() {
        dataSetup(User::class.java)
    }

    @Test
    fun `test createUser`() {
        val userIn = UserIn("User1", "user1@gmail.com", dummyPassword, dummyAvatar)
        val requestBody = Json.encodeToString(userIn)

        val response = app(Request(Method.POST, "$baseUrl/user").body(requestBody))
        val userOut = Json.decodeFromString<UserOut>(response.bodyString())

        assertEquals(Status.CREATED, response.status)
        assertEquals(response.header("content-type"), "application/json")
        assertEquals(1, userOut.idUser)
    }

    @Test
    fun `test create user with invalid mail`() {
        val userIn = UserIn(dummyName, dummyBadEmail, dummyPassword, dummyAvatar)

        val requestBody = Json.encodeToString(userIn)
        val response = app(Request(Method.POST, "$baseUrl/user").body(requestBody))
        val msg = Json.decodeFromString<String>(response.bodyString())

        assertEquals(Status.BAD_REQUEST, response.status)
        assertEquals("Invalid parameter: $dummyBadEmail", msg)
    }

    @Test
    fun `test create already existing user`() {
        createUser()
        val userIn2 = UserIn(dummyName, dummyEmail, dummyPassword, dummyAvatar)

        val requestBody = Json.encodeToString(userIn2)
        val response = app(Request(Method.POST, "$baseUrl/user").body(requestBody))
        val msg = Json.decodeFromString<String>(response.bodyString())

        assertEquals(Status.CONFLICT, response.status)
        assertEquals("email $dummyEmail already exists.", msg)
    }

    @Test
    fun `test create user without body`() {
        val response = app(Request(Method.POST, "$baseUrl/user"))

        assertEquals(Status.BAD_REQUEST, response.status)
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
            Request(Method.GET, "$baseUrl/user")
        )

        val msg = Json.decodeFromString<String>(response.bodyString())

        assertEquals(Status.UNAUTHORIZED, response.status)
        assertEquals("Unauthorized Operation.", msg)
    }
}
