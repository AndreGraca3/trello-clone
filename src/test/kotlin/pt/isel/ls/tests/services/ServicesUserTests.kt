package pt.isel.ls.tests.services

import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.User
import pt.isel.ls.tests.utils.createUser
import pt.isel.ls.tests.utils.dataSetup
import pt.isel.ls.tests.utils.dummyBadEmail
import pt.isel.ls.tests.utils.dummyEmail
import pt.isel.ls.tests.utils.dummyName
import pt.isel.ls.tests.utils.invalidToken
import pt.isel.ls.tests.utils.services
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ServicesUserTests {

    @BeforeTest
    fun setup() {
        dataSetup(User::class.java)
    }

    @Test
    fun `Create a valid user`() {
        val user = services.userServices.createUser(dummyName, dummyEmail)
        assertEquals(1, user.first)
    }

    @Test
    fun `Create a user with invalid email`() {
        val err = assertFailsWith<TrelloException.IllegalArgument> {
            services.userServices.createUser(dummyName, dummyBadEmail)
        }
        assertEquals(400, err.status.code)
        assertEquals("Invalid parameters: $dummyBadEmail", err.message)
    }

    @Test
    fun `Get existing user`() {
        val token = createUser().second
        val user = services.userServices.getUser(token)
        assertEquals(1, user.idUser)
        assertEquals(dummyName, user.name)
        assertEquals(dummyEmail, user.email)
    }

    @Test
    fun `Get non-existing user`() {
        createUser()
        val err = assertFailsWith<TrelloException.NotAuthorized> { services.userServices.getUser(invalidToken) }
        assertEquals(401, err.status.code)
        assertEquals("Unauthorized Operation.", err.message)
    }
}
