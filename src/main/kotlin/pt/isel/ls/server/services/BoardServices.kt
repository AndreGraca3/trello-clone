package pt.isel.ls.server.services

import pt.isel.ls.server.data.dataInterfaces.BoardData
import pt.isel.ls.server.data.dataInterfaces.UserBoardData
import pt.isel.ls.server.data.dataInterfaces.UserData
import pt.isel.ls.server.utils.Board
import pt.isel.ls.server.utils.isValidString

class BoardServices(
    private val userData: UserData,
    private val boardData: BoardData,
    private val userBoardData: UserBoardData
) {

    /** ------------------------------- *
     *         Board Management         *
     *  ------------------------------ **/

    fun createBoard(token: String, name: String, description: String): Int {
        isValidString(name)
        isValidString(description)
        boardData.checkBoardName(name)
        val idUser = userData.getUser(token).idUser
        val idBoard = boardData.createBoard(idUser, name, description)
        userBoardData.addUserToBoard(idUser,idBoard)
        return idBoard
    }

    fun getBoard(token: String, idBoard: Int): Board {
        val idUser = userData.getUser(token).idUser
        userBoardData.checkUserInBoard(idUser, idBoard)
        return boardData.getBoard(idBoard)
    }

    fun getBoardsFromUser(token: String): List<Board> {
        val idUser = userData.getUser(token).idUser
        val boardsIds = userBoardData.searchUserBoards(idUser)
        return boardData.getBoardsFromUser(boardsIds)
    }

    fun addUserToBoard(token: String, idNewUser: Int, idBoard: Int) {
        val idUser = userData.getUser(token).idUser
        userBoardData.checkUserInBoard(idUser, idBoard)
        userData.getUser(idNewUser) // check if user to add exists
        try {
            userBoardData.checkUserInBoard(idNewUser, idBoard)    // this throws exception
        } catch (e: Exception) {
            userBoardData.addUserToBoard(idNewUser, idBoard)
        }
    }
}