package pt.isel.ls.utils

import pt.isel.ls.server.Services
import pt.isel.ls.server.data.DataMem
import pt.isel.ls.server.data.initialState
import pt.isel.ls.server.exceptions.TrelloException
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ServicesTests {

    private val dummyName = "Alberto"
    private val dummyEmail = "alberto.tremocos@gmail.com"
    private val dummyBadEmail = dummyEmail.replace("@", "")

    private val data = DataMem()
    private val services = Services(data)

    /**
     * We delete all data from DataMem in order to have a known initial state of data.
     */
    @BeforeTest
    fun setup() {
        initialState()
    }

    @Test
    fun `Create a valid user`() {
        val user = services.createUser(dummyName, dummyEmail)
        assertEquals(0, user.first)
    }

    @Test
    fun `Create a user with invalid email`() {
        val err = assertFailsWith<TrelloException.IllegalArgument> {
            services.createUser(dummyName, dummyBadEmail)
        }
        assertEquals(400, err.status.code)
        assertEquals("Invalid parameters: $dummyBadEmail", err.message)
    }

    @Test
    fun `Get existing user`() {
        services.createUser(dummyName, dummyEmail)
        val user = services.getUser(0)
        assertEquals(0, user.idUser)
        assertEquals(dummyName, user.name)
        assertEquals(dummyEmail, user.email)
    }

}