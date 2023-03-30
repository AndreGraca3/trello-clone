package pt.isel.ls.server

import org.http4k.routing.routes
import org.http4k.server.Jetty
import org.http4k.server.asServer
import pt.isel.ls.server.api.*
import pt.isel.ls.server.data.boardData.DataBoard
import pt.isel.ls.server.data.cardData.DataCard
import pt.isel.ls.server.data.listData.DataList
import pt.isel.ls.server.data.userData.DataUser
import pt.isel.ls.server.routes.BoardRoutes
import pt.isel.ls.server.routes.CardRoutes
import pt.isel.ls.server.routes.ListRoutes
import pt.isel.ls.server.routes.UserRoutes
import pt.isel.ls.server.services.BoardServices
import pt.isel.ls.server.services.CardServices
import pt.isel.ls.server.services.ListServices
import pt.isel.ls.server.services.UserServices

fun main() {
    val dataUser = DataUser()
    val dataBoard = DataBoard()
    val dataList = DataList()
    val dataCard = DataCard()

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
