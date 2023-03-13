package pt.isel.ls

import java.time.LocalDate

class Services(private val data: IData) {

    fun createUser(name : String, email : String) : User {
        val newUser = data.createUser(name,email)
        return User(newUser.second,email,name,newUser.first)
    }

    fun getUserInfo(idUser: Int): User? {
        return data.getUserInfo(idUser)
    }

    fun createBoard(idUser: Int, name: String, description: String): Int {
        return data.createBoard(idUser, name, description)
    }

    fun addUserToBoard(idUser: Int, idBoard: Int): Boolean {
        return data.addUserToBoard(idUser, idBoard)
    }

    fun getBoardsFromUser(idUser: Int): List<Board> {
        return data.getBoardsFromUser(idUser)
    }

    fun getBoardInfo(idBoard: Int): Board {
        return data.getBoardInfo(idBoard) ?: TODO("Error Module not implemented yet")
    }

    fun createNewListInBoard(idBoard: Int, name: String): Int {
        return data.createNewListInBoard(idBoard, name)
    }

    fun getListsOfBoard(idBoard: Int): List<BoardList> {
        return data.getListsOfBoard(idBoard)
    }

    fun getListInfo(idBoard: Int, idList: Int): BoardList {
        return data.getListInfo(idBoard, idList) ?: TODO("Error Module not implemented yet")
    }

    fun createCard(idList: Int, name: String, description: String, endDate: String): Int {
        return data.createCard(idList, name, description, endDate)
    }

    fun createCard(idList: Int, name: String, description: String): Int {
        return data.createCard(idList, name, description)
    }

    fun getCardsFromList(idBoard: Int, idList: Int): List<Card> {
        return data.getCardsFromList(idBoard, idList)
    }

    fun getCardInfoFromList(idBoard: Int, idList: Int, idCard: Int): Card {
        return data.getCardInfoFromList(idBoard, idList, idCard)
    }

    fun moveCard(idList: Int): Boolean {
        return data.moveCard(idList)
    }
}