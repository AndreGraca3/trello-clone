package pt.isel.ls.server

import org.http4k.routing.ResourceLoader
import org.http4k.routing.routes
import org.http4k.routing.singlePageApp
import org.http4k.server.Jetty
import org.http4k.server.asServer
import pt.isel.ls.server.api.WebAPI
import pt.isel.ls.server.data.dataMem.DataExecutorMem
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
    // val data = DataMem()
    // val executor = DataExecutorMem<Any>()
    val data = DataSQL()
    val executor = DataExecutorSQL<Any>()
    val services = Services(data, executor)
    val webAPI = WebAPI(services)

    val app = routes(
        UserRoutes(webAPI.userAPI)(),
        BoardRoutes(webAPI.boardAPI)(),
        ListRoutes(webAPI.listAPI)(),
        CardRoutes(webAPI.cardAPI)(),
        singlePageApp(ResourceLoader.Directory("static-content"))
    )

    val jettyServer = app.asServer(Jetty(8080)).start()
    logger.info("Server started listening...")

    readln()
    jettyServer.stop()

    logger.info("Server stopped Listening.")
}
