package pt.isel.ls.server

import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Jetty
import org.http4k.server.asServer
import pt.isel.ls.server.data.DataMem
import java.time.LocalDate


fun main(){
    val data = DataMem()
    val services = Services(data)
    val webApi = WebApi(services)

    val userRoutes = routes(
        "user" bind POST to webApi::postUser,
        "user/{idUser}" bind GET to webApi::getUserDetails,
        "board" bind POST to webApi::createBoard
    )

    val app = routes(
        userRoutes
    )

    val jettyServer = app.asServer(Jetty(8080)).start()
    logger.info("Server started listening...")

    readln()
    jettyServer.stop()

    logger.info("Server stopped Listening.")
}