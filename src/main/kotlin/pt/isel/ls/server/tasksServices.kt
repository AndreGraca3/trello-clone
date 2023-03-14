package pt.isel.ls.server

import pt.isel.ls.Board
import pt.isel.ls.BoardList
import pt.isel.ls.Card
import pt.isel.ls.User
import pt.isel.ls.server.data.*
import pt.isel.ls.server.exceptions.ServiceException
import java.time.LocalDate

class Services(private val data: IData) {
    fun createUser(name: String, email: String): Pair<Int, String> {
        if(!Regex("@").containsMatchIn(email)) throw ServiceException.InvalidEmailException(email)
        if(data.getUserByEmail(email) != null) throw ServiceException.UserCreationException(email)
        return data.createUser(name, email)
    }

    fun getUserInfo(idUser: Int): User {
        return data.getUserInfo(idUser) ?: throw ServiceException.UserNotFoundException(idUser)
    }

    fun getIdUserByToken(token: String): Int {
        return data.getIdUserByToken(token) ?: throw ServiceException.UserNotFoundWithTokenException(token) //created new exception
    }

    fun createBoard(idUser: Int, name: String, description: String): Int {
        getUserInfo(idUser)
        data.getBoardByName(name) ?: throw ServiceException.BoardDuplicateNameException(name)
        return data.createBoard(idUser, name, description)
    }

    fun addUserToBoard(idUser: Int, idBoard: Int) {
        getUserInfo(idUser)
        val board = getBoardInfo(idBoard)
        data.addUserToBoard(idUser, board)
    }

    fun getBoardsFromUser(idUser: Int): List<Board> {
        getUserInfo(idUser)
        return data.getBoardsFromUser(idUser)
    }

    fun getBoardInfo(idBoard: Int): Board {
        return data.getBoardInfo(idBoard) ?: throw ServiceException.BoardNotFoundException(idBoard)
    }

    fun createNewListInBoard(idBoard: Int, name: String): Int {
        getBoardInfo(idBoard)
        return data.createNewListInBoard(idBoard, name)
    }

    fun getListsOfBoard(idBoard: Int): List<BoardList> {
        getBoardInfo(idBoard)
        return data.getListsOfBoard(idBoard)
    }

    fun getListInfo(idList: Int): BoardList {
        return data.getListInfo(idList) ?: throw ServiceException.ListNotFoundException(idList)
    }

    fun createCard(idList: Int, name: String, description: String, endDate: String): Int {
        getListInfo(idList)
        checkEndDate(endDate)
        return data.createCard(idList, name, description, endDate)
    }

    private fun checkEndDate(endDate: String) {
        val endDateParsed = LocalDate.parse(endDate) // 2023-03-14
        if(endDateParsed < LocalDate.now()) throw ServiceException.CardCreationDateException(endDate)
    }

    fun createCard(idList: Int, name: String, description: String): Int {
        getListInfo(idList)
        return data.createCard(idList, name, description)
    }

    fun getCardsFromList(idList: Int): List<Card> {
        getListInfo(idList)
        return data.getCardsFromList(idList)
    }

    fun getCardInfo(idCard: Int): Card {
        return data.getCardInfo(idCard) ?: throw ServiceException.CardNotFoundException(idCard)
    }

    fun moveCard(idCard:Int, idList: Int) {
        val card = getCardInfo(idCard)
        return data.moveCard(card,idList)
    }
}