package pt.isel.ls.tests.utils

import pt.isel.ls.server.utils.Board
import pt.isel.ls.server.utils.BoardList
import pt.isel.ls.server.utils.Card
import pt.isel.ls.server.utils.User

//Functions to create dummy components for tests
fun createUser() = dataUser.createUser(dummyName, dummyEmail)

fun createBoard(idUser: Int) = dataBoard.createBoard(idUser, dummyBoardName, dummyBoardDescription)

fun createList(idBoard: Int) = dataList.createList(idBoard, dummyBoardListName)

//fun createCard(idList: Int) = dataCard.createCard(idList, dummyCardName ,dummyCardDescription)


/** Every Test empties data
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

fun initialState() { // for test purposes
    dataUser.users.clear()
    dataUserBoard.usersBoards.clear()
    dataBoard.boards.clear()
    dataList.lists.clear()
    dataCard.cards.clear()
}