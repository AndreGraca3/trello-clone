package pt.isel.ls.server

import org.http4k.core.Method.*
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Jetty
import org.http4k.server.asServer
import pt.isel.ls.server.data.DataMem


fun main() {
    /** alterar a documentação para ficar de acordo com o que implementá-mos **/

    val data = DataMem()
    val services = Services(data)
    val webApi = WebApi(services)

    val userRoutes = routes(
        "user" bind POST to webApi::createUser,
        "user/{idUser}" bind GET to webApi::getUser
    )

    val boardRoutes = routes(
        "board" bind POST to webApi::createBoard,
        "board/{idBoard}/{idUser}" bind PUT to webApi::addUserToBoard,
        /** como é que defino parametros na query string? **/
        "board/{idBoard}" bind GET to webApi::getBoard,
        "allBoard" bind GET to webApi::getBoardsFromUser
    )

    val listRoutes = routes(
        "board/list/?idBoard&name" bind PUT to webApi::createList,
        /** Deveria ser um post? **/
        "board/list/{idBoard}/{idList}" bind GET to webApi::getList,
        /** opinião em relação ao path **/
        "board/allList/{idBoard}" bind GET to webApi::getListsFromBoard
    )

    val cardRoutes = routes(
        "cards" bind POST to webApi::createCard,
        "cards" bind GET to webApi::getCard
    )

    val app = routes(
        userRoutes,
        boardRoutes,
        listRoutes,
        cardRoutes
    )

    val jettyServer = app.asServer(Jetty(8080)).start()
    logger.info("Server started listening...")

    readln()
    jettyServer.stop()

    logger.info("Server stopped Listening.")
}