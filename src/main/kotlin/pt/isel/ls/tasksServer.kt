package pt.isel.ls

import kotlinx.datetime.Clock
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import org.http4k.server.Jetty
import org.http4k.server.asServer


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