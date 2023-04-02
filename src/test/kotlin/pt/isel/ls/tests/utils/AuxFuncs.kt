package pt.isel.ls.tests.utils

import pt.isel.ls.server.utils.Board
import pt.isel.ls.server.utils.BoardList
import pt.isel.ls.server.utils.Card
import pt.isel.ls.server.utils.User

// Functions to create dummy components for tests
fun createUser(name: String = dummyName, email: String = dummyEmail) = dataMem.userData.createUser(name, email)

fun createBoard(
    idUser: Int,
    boardName: String = dummyBoardName,
    boardDescription: String = dummyBoardDescription
): Int {
    val idBoard = dataMem.boardData.createBoard(idUser, boardName, boardDescription)
    dataMem.userBoardData.addUserToBoard(idUser, idBoard)
    return idBoard
}

fun createList(idBoard: Int, listName: String = dummyBoardListName) = dataMem.listData.createList(idBoard, listName)

fun createCard(
    idList: Int,
    idBoard: Int,
    cardName: String = dummyCardName,
    cardDescription: String = dummyCardDescription,
    endDate: String? = null
) =
    dataMem.cardData.createCard(idList, idBoard, cardName, cardDescription, endDate)

/** Empties data for every Test
 * and creates only necessary components **/
fun dataSetup(clazz: Class<*>) {
    initialState()
    if (clazz.simpleName == User::class.simpleName) return

    val userPair = createUser()
    user = User(userPair.first, dummyEmail, dummyName, userPair.second)
    if (clazz.simpleName == Board::class.simpleName) return

    boardId = createBoard(user.idUser)
    if (clazz.simpleName == BoardList::class.simpleName) return

    listId = createList(boardId)
    if (clazz.simpleName == Card::class.simpleName) return

    throw IllegalArgumentException("Class not supported")
}

fun initialState() {
    dataMem.userData.users.clear()
    dataMem.userBoardData.usersBoards.clear()
    dataMem.boardData.boards.clear()
    dataMem.listData.lists.clear()
    dataMem.cardData.cards.clear()
}
