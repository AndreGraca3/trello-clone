package pt.isel.ls.server

import org.http4k.routing.ResourceLoader
import org.http4k.routing.routes
import org.http4k.routing.singlePageApp
import org.http4k.server.Jetty
import org.http4k.server.asServer
import pt.isel.ls.server.api.WebAPI
import pt.isel.ls.server.data.dataInterfaces.DataExecutor
import pt.isel.ls.server.data.dataMem.DataMem
import pt.isel.ls.server.data.dataPostGres.dataSQL.DataExecutorSQL
import pt.isel.ls.server.data.dataPostGres.dataSQL.DataSQL
import pt.isel.ls.server.routes.BoardRoutes
import pt.isel.ls.server.routes.CardRoutes
import pt.isel.ls.server.routes.ListRoutes
import pt.isel.ls.server.routes.UserRoutes
import pt.isel.ls.server.services.Services
import pt.isel.ls.server.utils.logger

fun main() {
    //val data = DataMem()
    val data = DataSQL()
    val services = Services(data, DataExecutorSQL())
    val webAPI = WebAPI(services)

    val app = routes(
        UserRoutes(webAPI.userAPI)(),
        BoardRoutes(webAPI.boardAPI)(),
        ListRoutes(webAPI.listAPI)(),
        CardRoutes(webAPI.cardAPI)(),
        singlePageApp(ResourceLoader.Directory("static-content"))
    )

    /*repeat(2) {
        data.boardData.createBoard(1, "Board $it", "this is $it")
        data.userBoardData.addUserToBoard(1,it)
    }
    repeat(3) {
        data.listData.createList(0, "List $it")
    }
    repeat(10) {
        data.cardData.createCard(it % 2, 0, "Card $it")
    }*/

    val jettyServer = app.asServer(Jetty(8080)).start()
    logger.info("Server started listening...")

    readln()
    jettyServer.stop()

    logger.info("Server stopped Listening.")
}
