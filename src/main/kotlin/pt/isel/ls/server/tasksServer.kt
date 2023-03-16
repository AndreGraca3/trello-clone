package pt.isel.ls.server

import org.http4k.core.Method.*
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Jetty
import org.http4k.server.asServer
import pt.isel.ls.server.data.DataMem
import java.time.LocalDate


fun main(){
    /** alterar a documentação para ficar de acordo com o que implementá-mos **/

    val data = DataMem()
    val services = Services(data)
    val webApi = WebApi(services)

    val userRoutes = routes(
        "user" bind POST to webApi::postUser,
        "user/{idUser}" bind GET to webApi::getUserInfo
    )

    val boardRoutes = routes(
        "board" bind POST to webApi::createBoard,
        "board/{idBoard}/{idUser}" bind PUT to webApi::addUserToBoard, /** como é que defino parametros na query string? **/
        "board/{idBoard}" bind GET to webApi::getBoardInfo,
        "allBoard" bind GET to webApi::getBoardsFromUser
    )

    val listRoutes = routes(
        "board/list/?idBoard&name" bind PUT to webApi::createNewListInBoard, /** Deveria ser um post? **/
        "board/list/{idBoard}/{idList}" bind GET to webApi::getListInfo, /** opinião em relação ao path **/
        "board/allList/{idBoard}" bind GET to webApi::getListFromBoard
    )

    /*val cardRoutes = routes(
        "cards" bind GET to webApi::noWhere
    )*/

    val app = routes(
        userRoutes,
        boardRoutes,
        listRoutes,
        //cardRoutes
    )

    val jettyServer = app.asServer(Jetty(8080)).start()
    logger.info("Server started listening...")

    readln()
    jettyServer.stop()

    logger.info("Server stopped Listening.")
}