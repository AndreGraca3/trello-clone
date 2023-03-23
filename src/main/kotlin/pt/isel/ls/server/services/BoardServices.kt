package pt.isel.ls.server.services

import pt.isel.ls.Board
import pt.isel.ls.server.data.boardData.IDataBoard
import pt.isel.ls.server.data.cardData.IDataCard
import pt.isel.ls.server.data.listData.IDataList
import pt.isel.ls.server.data.userData.IUserData
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.isValidString

class BoardServices(private val userServices: UserServices, private val boardData: IDataBoard) {

    fun createBoard(token: String, name: String, description: String): Int {
        isValidString(name)
        isValidString(description)
        if (boardData.getBoardByName(name) != null) throw TrelloException.AlreadyExists(name)
        val user = userServices.getUser(token)
        return boardData.createBoard(user.idUser, name, description)
    }

    fun getBoard(token: String, idBoard: Int): Board {  //already verifies if Board belongs to User and if he exists
        val idUser = userServices.getUser(token).idUser
        if (!checkUserInBoard(idUser, idBoard)) throw TrelloException.NotFound("Board")
        return boardData.getBoard(idBoard) ?: throw TrelloException.NotFound("Board")
    }

    fun getBoardsFromUser(token: String): List<Board> {
        val user = userServices.getUser(token)
        return boardData.getBoardsFromUser(user.idUser)
    }

    fun addUserToBoard(token: String, idUser: Int, idBoard: Int) {
        val newUser = userServices.getUser(idUser)   //user to add
        if (checkUserInBoard(newUser.idUser, idBoard)) return    //Idempotent Operation
        boardData.addUserToBoard(idUser, getBoard(token, idBoard))
    }

    private fun checkUserInBoard(idUser: Int, idBoard: Int): Boolean {
        return boardData.checkUserInBoard(idUser, idBoard)
    }
}