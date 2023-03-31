package pt.isel.ls.server

import org.http4k.routing.routes
import org.http4k.server.Jetty
import org.http4k.server.asServer
import pt.isel.ls.server.api.*
import pt.isel.ls.server.data.dataMem.BoardDataMem
import pt.isel.ls.server.data.dataMem.CardDataMem
import pt.isel.ls.server.data.dataMem.ListDataMem
import pt.isel.ls.server.data.dataMem.UserDataMem
import pt.isel.ls.server.data.dataPostGres.dataSQL.UserDataSQL
import pt.isel.ls.server.routes.BoardRoutes
import pt.isel.ls.server.routes.CardRoutes
import pt.isel.ls.server.routes.ListRoutes
import pt.isel.ls.server.routes.UserRoutes
import pt.isel.ls.server.services.BoardServices
import pt.isel.ls.server.services.CardServices
import pt.isel.ls.server.services.ListServices
import pt.isel.ls.server.services.UserServices

fun main() {
    //val dataUser = UserDataMem()
    val dataUser = UserDataSQL()
    val dataBoard = BoardDataMem()
    val dataList = ListDataMem()
    val dataCard = CardDataMem()

    val servicesUser = UserServices(dataUser)
    val servicesBoard = BoardServices(dataBoard)
    val servicesList = ListServices(dataList)
    val servicesCard = CardServices(dataCard)

    val userApi = UserWebApi(servicesUser)
    val boardApi = BoardWebApi(servicesBoard)
    val listApi = ListWebApi(servicesList)
    val cardApi = CardWebApi(servicesCard)

    val app = routes(
        UserRoutes(userApi)(),
        BoardRoutes(boardApi)(),
        ListRoutes(listApi)(),
        CardRoutes(cardApi)()
    )

    val jettyServer = app.asServer(Jetty(8080)).start()
    logger.info("Server started listening...")

    readln()
    jettyServer.stop()

    logger.info("Server stopped Listening.")
}
