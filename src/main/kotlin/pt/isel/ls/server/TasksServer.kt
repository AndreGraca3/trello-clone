package pt.isel.ls.server

import org.http4k.routing.routes
import org.http4k.server.Jetty
import org.http4k.server.asServer
import pt.isel.ls.server.data.DataMem
import pt.isel.ls.server.routes.*
import pt.isel.ls.server.services.Services
import pt.isel.ls.server.web.api.TasksApi
import pt.isel.ls.server.web.api.UserAPI
import pt.isel.ls.server.web.api.logger

const val PORT = 8080

fun main() {

    val data = DataMem()
    val services = Services(data)
    val webApi = TasksApi(services)

    val app = routes(
        UserRoutes(webApi.userAPI)(),
        BoardRoutes(webApi.boardAPI)(),
        ListRoutes(webApi.listAPI)(),
        CardRoutes(webApi.cardAPI)(),
    )

    val jettyServer = app.asServer(Jetty(PORT)).start()
    logger.info("Server started listening...")

    readln()
    jettyServer.stop()

    logger.info("Server stopped Listening.")
}
