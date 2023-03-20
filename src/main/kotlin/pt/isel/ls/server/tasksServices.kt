package pt.isel.ls.server

import pt.isel.ls.Board
import pt.isel.ls.BoardList
import pt.isel.ls.Card
import pt.isel.ls.User
import pt.isel.ls.server.data.*
import pt.isel.ls.server.exceptions.TrelloException
import java.time.LocalDate

class Services(private val data: IData) {

    /** ----------------------------
     *  User Management
     *  ------------------------------**/

    fun createUser(name: String, email: String): Pair<Int, String> {
        isValidString(name)
        isValidString(email)
        if (!Regex("@").containsMatchIn(email)) throw TrelloException.IllegalArgument(email)
        if (!data.checkEmail(email)) throw TrelloException.AlreadyExists(email)
        return data.createUser(name, email)
    }

    fun getUser(token: String): User {
        return data.getUser(token) ?: throw TrelloException.NotFound("User")    //not sure
    }

    fun getUser(id: Int): User {
        return data.getUser(id) ?: throw TrelloException.NotFound("User")
    }

    /** ----------------------------
     *  Board Management
     *  ------------------------------**/

    fun createBoard(token: String, name: String, description: String): Int {
        isValidString(name)
        isValidString(description)
        if (data.getBoardByName(name) != null) throw TrelloException.AlreadyExists(name)
        val user = getUser(token)
        return data.createBoard(user.idUser, name, description)
    }

    fun getBoard(token: String, idBoard: Int): Board {  //already verifies if Board belongs to User and if he exists
        val idUser = getUser(token).idUser
        if (!checkUserInBoard(idUser, idBoard)) throw TrelloException.NotFound("Board")
        return data.getBoard(idBoard) ?: throw TrelloException.NotFound("Board")
    }

    fun getBoardsFromUser(token: String): List<Board> {
        val user = getUser(token)
        return data.getBoardsFromUser(user.idUser)
    }

    fun addUserToBoard(token: String, idUser: Int, idBoard: Int) {
        val newUser = getUser(idUser)   //user to add
        if (checkUserInBoard(newUser.idUser, idBoard)) return    //Idempotent Operation
        data.addUserToBoard(idUser, getBoard(token, idBoard))
    }

    private fun checkUserInBoard(idUser: Int, idBoard: Int): Boolean {
        return data.checkUserInBoard(idUser, idBoard)
    }

    /** ----------------------------
     *  List Management
     *  ------------------------------**/

    fun createList(token: String, idBoard: Int, name: String): Int {
        isValidString(name)
        getBoard(token, idBoard)
        return data.createList(idBoard, name)
    }

    fun getList(token: String, idList: Int): BoardList {
        val list = data.getList(idList) ?: throw TrelloException.NotFound("BoardList")
        getBoard(token, list.idBoard)
        return list
    }

    fun getListsOfBoard(token: String, idBoard: Int): List<BoardList> {
        getBoard(token, idBoard)
        return data.getListsOfBoard(idBoard)
    }

    /** ----------------------------
     *  Card Management
     *  ------------------------------**/

    fun createCard(token: String, idList: Int, name: String, description: String, endDate: String?): Int {
        isValidString(name)
        isValidString(description)
        if (endDate != null) checkEndDate(endDate)
        getList(token, idList)
        return data.createCard(idList, name, description, endDate)
    }

    fun getCard(token: String, idBoard: Int, idList: Int, idCard: Int): Card {
        val card = data.getCard(idCard) ?: throw TrelloException.NotFound("Card")
        getList(token, card.idList)
        return card
        //TODO
    }

    fun getCardsFromList(token: String, idList: Int): List<Card> {
        getList(token, idList)
        return data.getCardsFromList(idList)
    }

    fun moveCard(token: String, idCard: Int, idList: Int) {
        val card = getCard(token, idCard, idList, idCard)
        getList(token, idList)
        return data.moveCard(card, idList)
    }
}

//Aux Functions
private fun checkEndDate(endDate: String) {
    val endDateParsed = LocalDate.parse(endDate) // 2023-03-14
    if (endDateParsed < LocalDate.now()) throw TrelloException.IllegalArgument(endDate)
}

private fun isValidString(value: String): Boolean {
    val trim = value.trim()
    if (trim != "" && trim != "null") return true
    throw TrelloException.IllegalArgument(value)
}