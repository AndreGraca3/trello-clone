package pt.isel.ls.server.services

import pt.isel.ls.server.data.dataInterfaces.BoardData
import pt.isel.ls.server.data.checkUserInBoard
import pt.isel.ls.server.data.getUser
import pt.isel.ls.server.utils.Board
import pt.isel.ls.server.utils.isValidString

class BoardServices(private val boardData: BoardData) {

    /** ------------------------------- *
     *         Board Management         *
     *  ------------------------------ **/

    fun createBoard(token: String, name: String, description: String): Int {
        isValidString(name)
        isValidString(description)
        boardData.getBoardByName(name)
        val idUser = getUser(token).idUser
        return boardData.createBoard(idUser, name, description)
    }

    fun getBoard(token: String, idBoard: Int): Board {
        val idUser = getUser(token).idUser
        checkUserInBoard(idUser, idBoard)
        return boardData.getBoard(idBoard)
    }

    fun getBoardsFromUser(token: String): List<Board> {
        val idUser = getUser(token).idUser
        return boardData.getBoardsFromUser(idUser)
    }

    fun addUserToBoard(token: String, idNewUser: Int, idBoard: Int) {
        val idUser = getUser(token).idUser
        checkUserInBoard(idUser, idBoard)
        getUser(idNewUser) // check if user to add exists
        kotlin.runCatching {
            checkUserInBoard(idNewUser, idBoard)    // this throws exception
            boardData.addUserToBoard(idNewUser, idBoard)
        }
    }
}