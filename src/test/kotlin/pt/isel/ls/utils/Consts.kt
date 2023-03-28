package pt.isel.ls.utils

import org.http4k.core.Method
import org.http4k.routing.bind
import org.http4k.routing.routes
import pt.isel.ls.server.Services
import pt.isel.ls.server.WebApi
import pt.isel.ls.server.data.boardData.DataBoard
import pt.isel.ls.server.data.cardData.DataCard
import pt.isel.ls.server.data.listData.DataList
import pt.isel.ls.server.data.userData.DataUser

const val invalidToken = "INVALID_TOKEN"
const val invalidEndDate = "INVALID_END_DATE"

/** User Dummies **/
const val dummyName = "Alberto"
const val dummyEmail = "alberto.tremocos@gmail.com"
val dummyBadEmail = dummyEmail.replace("@", "")

/** Board Dummies **/
const val dummyBoardName = "Board1"
const val dummyBoardDescription = "This is Board1"

/** BoardList Dummies **/
const val dummyBoardListName = "List1"

/** Card Dummies **/
const val dummyCardName = "Card1"
const val validEndDate = "2023-12-12"
const val dummyCardDescription = "This is Card1"

/** Base Url **/
const val baseUrl = "http://localhost:8080"

/** modules **/
val dataUser = DataUser()
val dataBoard = DataBoard()
val dataList = DataList()
val dataCard = DataCard()
val webApi = WebApi(Services(dataUser, dataBoard, dataList, dataCard))

/** routes **/
val app = routes(
    "user" bind Method.POST to webApi::createUser,
    "user" bind Method.GET to webApi::getUser,
    "board" bind Method.POST to webApi::createBoard,
    "board" bind Method.GET to webApi::getBoardsFromUser,
    "board/{idBoard}" bind Method.PUT to webApi::addUserToBoard,
    "board/{idBoard}" bind Method.GET to webApi::getBoard,
    "board/{idBoard}/list" bind Method.POST to webApi::createList,
    "board/{idBoard}/list" bind Method.GET to webApi::getListsFromBoard,
    "board/{idBoard}/list/{idList}" bind Method.GET to webApi::getList,
    "board/{idBoard}/list/{idList}/card" bind Method.POST to webApi::createCard,
    "board/{idBoard}/list/{idList}/card" bind Method.GET to webApi::getCardsFromList,
    "board/{idBoard}/list/{idList}/card/{idCard}" bind Method.GET to webApi::getCard,
    "board/{idBoard}/list/{idList}/card/{idCard}" bind Method.PUT to webApi::moveCard /** idList destination comes in body.**/
)
