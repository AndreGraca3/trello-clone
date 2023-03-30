package pt.isel.ls.server

import pt.isel.ls.server.data.boardData.DataBoard
import pt.isel.ls.server.data.cardData.DataCard
import pt.isel.ls.server.data.listData.DataList
import pt.isel.ls.server.data.userData.DataUser
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.Board
import pt.isel.ls.server.utils.BoardList
import pt.isel.ls.server.utils.Card
import pt.isel.ls.server.utils.User
import pt.isel.ls.server.utils.checkEndDate
import pt.isel.ls.server.utils.isValidString

class Services(
    private val userData: DataUser,
    private val boardData: DataBoard,
    private val listData: DataList,
    private val cardData: DataCard
) {

    /** ------------------------------ *
     *         User Management         *
     *  ---------------------------- **/

    fun createUser(name: String, email: String): Pair<Int, String> {
        isValidString(name)
        isValidString(email)
        if (!Regex("@").containsMatchIn(email)) throw TrelloException.IllegalArgument(email)
        if (!userData.checkEmail(email)) throw TrelloException.AlreadyExists(email)
        return userData.createUser(name, email)
    }

    fun getUser(token: String): User {
        return userData.getUser(token) ?: throw TrelloException.NotAuthorized() // not sure
    }

    fun getUser(id: Int): User {
        return userData.getUser(id) ?: throw TrelloException.NotFound("User")
    }

    /** ------------------------------- *
     *         Board Management         *
     *  ------------------------------ **/

    fun createBoard(token: String, name: String, description: String): Int {
        isValidString(name)
        isValidString(description)
        if (boardData.getBoardByName(name) != null) throw TrelloException.AlreadyExists(name)
        val user = getUser(token)
        return boardData.createBoard(user.idUser, name, description)
    }

    fun getBoard(token: String, idBoard: Int): Board { // already verifies if Board belongs to User and if he exists
        val idUser = getUser(token).idUser
        if (!checkUserInBoard(idUser, idBoard)) throw TrelloException.NotFound("Board")
        return boardData.getBoard(idBoard) ?: throw TrelloException.NotFound("Board")
    }

    fun getBoardsFromUser(token: String): List<Board> {
        val user = getUser(token)
        return boardData.getBoardsFromUser(user.idUser)
    }

    fun addUserToBoard(token: String, idUser: Int, idBoard: Int) {
        val newUser = getUser(idUser) // user to add
        if (checkUserInBoard(newUser.idUser, idBoard)) return // Idempotent Operation
        boardData.addUserToBoard(idUser, getBoard(token, idBoard))
    }

    private fun checkUserInBoard(idUser: Int, idBoard: Int): Boolean {
        return boardData.checkUserInBoard(idUser, idBoard)
    }

    /** ------------------------------ *
     *         List Management         *
     *  ----------------------------- **/

    fun createList(token: String, idBoard: Int, name: String): Int {
        isValidString(name)
        getBoard(token, idBoard)
        return listData.createList(idBoard, name)
    }

    fun getList(token: String, idBoard: Int, idList: Int): BoardList {
        val list = listData.getList(idList) ?: throw TrelloException.NotFound("BoardList")
        if(getBoard(token, idBoard) != getBoard(token, list.idBoard)) throw TrelloException.NotFound("BoardList")
        return list
    }

    fun getListsOfBoard(token: String, idBoard: Int): List<BoardList> {
        getBoard(token, idBoard)
        return listData.getListsOfBoard(idBoard)
    }

    /** -----------------------------  *
     *         Card Management         *
     *  ----------------------------- **/

    fun createCard(token: String, idBoard: Int, idList: Int, name: String, description: String, endDate: String?): Int {
        isValidString(name)
        isValidString(description)
        if (endDate != null) checkEndDate(endDate)
        getList(token, idBoard,idList)
        return cardData.createCard(idList, name, description, endDate)
    }

    fun getCard(token: String, idBoard: Int, idList: Int, idCard: Int): Card {
        val card = cardData.getCard(idCard) ?: throw TrelloException.NotFound("Card")
        getList(token, idBoard, idList)
        return card
    }

    fun getCardsFromList(token: String, idBoard: Int, idList: Int): List<Card> {
        getList(token,idBoard, idList)
        return cardData.getCardsFromList(idList)
    }

    fun moveCard(token: String, idBoard: Int, idListNow: Int, idListDst: Int, idCard: Int) {
        val card = getCard(token, idBoard, idListNow, idCard) // verifies that card "belongs" to the user.
        getList(token, idBoard, idListDst) // verifies that listDst "belongs" to the user.
        return cardData.moveCard(card, idListDst)
    }
}