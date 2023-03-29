package pt.isel.ls.tests.utils

import org.http4k.routing.routes
import pt.isel.ls.server.Services
import pt.isel.ls.server.WebApi
import pt.isel.ls.server.data.boardData.DataBoard
import pt.isel.ls.server.data.cardData.DataCard
import pt.isel.ls.server.data.listData.DataList
import pt.isel.ls.server.data.userData.DataUser
import pt.isel.ls.server.routes.BoardRoutes
import pt.isel.ls.server.routes.CardRoutes
import pt.isel.ls.server.routes.ListRoutes
import pt.isel.ls.server.routes.UserRoutes
import pt.isel.ls.server.utils.User

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

/** tests initial components **/
var user = User(0, dummyEmail, dummyName, "token")
var boardId = 0
var listId = 0

/** routes **/
val app = routes(
    UserRoutes(webApi)(),
    BoardRoutes(webApi)(),
    ListRoutes(webApi)(),
    CardRoutes(webApi)(),
)
