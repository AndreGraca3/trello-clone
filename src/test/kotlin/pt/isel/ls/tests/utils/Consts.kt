package pt.isel.ls.tests.utils

import org.http4k.routing.routes
import pt.isel.ls.server.api.BoardWebApi
import pt.isel.ls.server.api.CardWebApi
import pt.isel.ls.server.api.ListWebApi
import pt.isel.ls.server.api.UserWebApi
import pt.isel.ls.server.data.dataInterfaces.UserBoardData
import pt.isel.ls.server.data.dataMem.*
import pt.isel.ls.server.routes.BoardRoutes
import pt.isel.ls.server.routes.CardRoutes
import pt.isel.ls.server.routes.ListRoutes
import pt.isel.ls.server.routes.UserRoutes
import pt.isel.ls.server.services.BoardServices
import pt.isel.ls.server.services.CardServices
import pt.isel.ls.server.services.ListServices
import pt.isel.ls.server.services.UserServices
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
val dataUser = UserDataMem()
val dataUserBoard = UserBoardDataMem()
val dataBoard = BoardDataMem()
val dataList = ListDataMem()
val dataCard = CardDataMem()

val userServices = UserServices(dataUser)
val boardServices = BoardServices(dataBoard, dataUserBoard, dataUser)
val listServices = ListServices(dataList)
val cardServices = CardServices(dataCard)

val userApi = UserWebApi(userServices)
val boardApi = BoardWebApi(boardServices)
val listApi = ListWebApi(listServices)
val cardApi = CardWebApi(cardServices)

/** tests initial components **/
var user = User(0, dummyEmail, dummyName, "token")
var boardId = 0
var listId = 0

/** routes **/
val app = routes(
    UserRoutes(userApi)(),
    BoardRoutes(boardApi)(),
    ListRoutes(listApi)(),
    CardRoutes(cardApi)()
)
