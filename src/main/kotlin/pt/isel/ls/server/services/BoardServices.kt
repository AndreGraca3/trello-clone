package pt.isel.ls.server.services

import pt.isel.ls.server.data.dataInterfaces.BoardData
import pt.isel.ls.server.data.dataInterfaces.UserBoardData
import pt.isel.ls.server.data.dataInterfaces.UserData
import pt.isel.ls.server.data.getUser
import pt.isel.ls.server.utils.Board
import pt.isel.ls.server.utils.isValidString

class BoardServices(
    private val boardData: BoardData,
    private val userBoardData: UserBoardData,
    private val userData: UserData
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
        val idUser = getUser(token).idUser
        userBoardData.checkUserInBoard(idUser, idBoard)
        return boardData.getBoard(idBoard)
    }

    fun getBoardsFromUser(token: String): List<Board> {
        val idUser = getUser(token).idUser
        val boardsIds = userBoardData.searchUserBoards(idUser)
        return boardData.getBoardsFromUser(boardsIds)
    }

    fun addUserToBoard(token: String, idNewUser: Int, idBoard: Int) {
        val idUser = getUser(token).idUser
        userBoardData.checkUserInBoard(idUser, idBoard)
        getUser(idNewUser) // check if user to add exists
        kotlin.runCatching {
            userBoardData.checkUserInBoard(idNewUser, idBoard)    // this throws exception
            userBoardData.addUserToBoard(idNewUser, idBoard)
        }
    }
}