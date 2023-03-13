package pt.isel.ls

import java.time.LocalDate

class Services(private val data: IData) {
    fun getUserByEmail(email: String) : Int? { //check return type
        TODO()
    }
    fun createUser(name: String, email: String): Pair<String, Int> {
        getUserByEmail(email) ?: throw ServiceException.UserCreationException(name, email)
        return data.createUser(name, email)
    }

    fun getUserInfo(idUser: Int): User {
        return data.getUserInfo(idUser) ?: throw ServiceException.UserNotFoundException(idUser)
    }

    fun createBoard(idUser: Int, name: String, description: String): Int {
        data.getUserInfo(idUser) ?: throw ServiceException.BoardCreationException(idUser, name)
        return data.createBoard(idUser, name, description)
    }

    fun addUserToBoard(idUser: Int, idBoard: Int) { //check if return type is boolean or unit
        val board = getBoardInfo(idBoard)
        return data.addUserToBoard(idUser, board) ?: throw ServiceException.BoardNotFoundException(idBoard)
    }

    fun getBoardsFromUser(idUser: Int): List<Board> {
        return data.getBoardsFromUser(idUser) ?: throw ServiceException.UserBoardsException(idUser)
    }

    fun getBoardInfo(idBoard: Int): Board {
        return data.getBoardInfo(idBoard) ?: throw ServiceException.BoardNotFoundException(idBoard)
    }

    fun createNewListInBoard(idBoard: Int, name: String): Int {
        return data.createNewListInBoard(idBoard, name) ?: throw ServiceException.ListCreationOnBoardException(idBoard, name)
    }

    fun getListsOfBoard(idBoard: Int): List<BoardList> {
        return data.getListsOfBoard(idBoard) ?: throw ServiceException.BoardListsException(idBoard)
    }

    fun getListInfo(idList: Int): BoardList {
        return data.getListInfo(idList) ?: throw ServiceException.ListNotFoundException(idList)
    }

    fun createCard(idList: Int, name: String, description: String, endDate: String): Int {
        return data.createCard(idList, name, description, endDate) ?: throw ServiceException.CardCreationDateException(idList, endDate)
    }

    fun createCard(idList: Int, name: String, description: String): Int {
        return data.createCard(idList, name, description) ?: throw ServiceException.CardCreationException(idList, name)
    }

    fun getCardsFromList(idBoard: Int, idList: Int): List<Card> {
        return data.getCardsFromList(idBoard, idList) ?: throw ServiceException.CardListException(idBoard, idList)
    }

    fun getCardInfoFromList(idBoard: Int, idList: Int, idCard: Int): Card {
        return data.getCardInfoFromList(idBoard, idList, idCard)
            ?: throw ServiceException.CardNotFoundException(idBoard, idList, idCard)
    }

    fun moveCard(idList: Int): Boolean {
        return data.moveCard(idList) ?: throw ServiceException.MoveCardException(idList)
    }
}