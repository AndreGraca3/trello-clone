package pt.isel.ls.server

import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Method.PUT
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Jetty
import org.http4k.server.asServer
import pt.isel.ls.server.data.boardData.DataBoard
import pt.isel.ls.server.data.cardData.DataCard
import pt.isel.ls.server.data.listData.DataList
import pt.isel.ls.server.data.userData.DataUser

fun main() {
    /** alterar a documentação para ficar de acordo com o que implementá-mos **/

    val dataUser = DataUser()
    val dataBoard = DataBoard()
    val dataList = DataList()
    val dataCard = DataCard()

    val services = Services(dataUser, dataBoard, dataList, dataCard)
    val webApi = WebApi(services)

    val userRoutes = routes(
        "user" bind POST to webApi::createUser,
        "user" bind GET to webApi::getUser
    )

    val boardRoutes = routes(
        "board" bind POST to webApi::createBoard,
        "board" bind GET to webApi::getBoardsFromUser,
        "board/{idBoard}/{idUser}" bind PUT to webApi::addUserToBoard, /** verificar este path com o martin ( path ou body)**/
        "board/{idBoard}" bind GET to webApi::getBoard
    )

    val listRoutes = routes(
        "board/{idBoard}/list" bind POST to webApi::createList,
        "board/{idBoard}/list" bind GET to webApi::getListsFromBoard,
        "board/{idBoard}/list/{idList}" bind GET to webApi::getList /** requer idBody? **/
    )

    val cardRoutes = routes(
        "board/{idBoard}/list/{idList}/card" bind POST to webApi::createCard,
        "board/{idBoard}/list/{idList}/card" bind GET to webApi::getCardsFromList,
        "board/{idBoard}/list/{idList}/card/{idCard}" bind GET to webApi::getCard,
        "board/{idBoard}/list/{idList}/card/{idCard}" bind PUT to webApi::moveCard /** idList destination comes in body.**/
        /** patch **/
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
