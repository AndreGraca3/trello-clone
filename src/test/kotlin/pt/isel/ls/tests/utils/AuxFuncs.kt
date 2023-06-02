package pt.isel.ls.tests.utils

import pt.isel.ls.server.data.dataMem.boards
import pt.isel.ls.server.data.dataMem.cards
import pt.isel.ls.server.data.dataMem.lists
import pt.isel.ls.server.data.dataMem.users
import pt.isel.ls.server.data.dataMem.usersBoards
import pt.isel.ls.server.Board
import pt.isel.ls.server.BoardList
import pt.isel.ls.server.Card
import pt.isel.ls.server.User

// Functions to create dummy components for tests
fun createUser(name: String = dummyName, email: String = dummyEmail, hashedPassword : String = dummyPassword, avatar: String = dummyAvatar) =
    executorTest.execute { dataMem.userData.createUser(name, email,hashedPassword, avatar, it) }

fun createBoard(
    idUser: Int,
    boardName: String = dummyBoardName,
    boardDescription: String = dummyBoardDescription
): Int {
    return executorTest.execute {
        val idBoard = dataMem.boardData.createBoard(idUser, boardName, boardDescription, it)
        dataMem.userBoardData.addUserToBoard(idUser, idBoard, it)
        idBoard
    }
}

fun createList(idBoard: Int, listName: String = dummyBoardListName) =
    executorTest.execute { dataMem.listData.createList(idBoard, listName, it) }

fun createCard(
    idList: Int,
    idBoard: Int,
    cardName: String = dummyCardName,
    cardDescription: String = dummyCardDescription,
    endDate: String? = null
) =
    executorTest.execute {
        dataMem.cardData.createCard(idList, idBoard, cardName, cardDescription, endDate, it)
    }

/** Empties data for every Test
 * and creates only necessary components **/
fun dataSetup(clazz: Class<*>) {
    initialState()
    if (clazz.simpleName == User::class.simpleName) return

    val userPair = createUser()
    user = User(userPair.first, dummyEmail, dummyName, userPair.second, dummyPassword, dummyAvatar)
    if (clazz.simpleName == Board::class.simpleName) return

    boardId = createBoard(user.idUser)
    if (clazz.simpleName == BoardList::class.simpleName) return

    listId = createList(boardId)
    if (clazz.simpleName == Card::class.simpleName) return

    throw IllegalArgumentException("Class not supported")
}

fun initialState() {
    users.clear()
    usersBoards.clear()
    boards.clear()
    lists.clear()
    cards.clear()
}
