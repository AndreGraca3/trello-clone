package pt.isel.ls.tests.services

import pt.isel.ls.server.data.dataMem.BoardDataMem
import kotlin.test.BeforeTest
import pt.isel.ls.server.services.BoardServices

class ServicesCardTests {

    private val boardDataMem = BoardDataMem()
    private val boardServices = BoardServices(boardDataMem)

    @BeforeTest
    fun setup() {
        TODO("Daniel")
    }



}