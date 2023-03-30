package pt.isel.ls.server.services

import pt.isel.ls.server.data.boardData.DataBoard
import pt.isel.ls.server.data.checkUserInBoard
import pt.isel.ls.server.data.getUser
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.Board
import pt.isel.ls.server.utils.isValidString

class BoardServices(private val boardData: DataBoard) {

    /** ------------------------------- *
     *         Board Management         *
     *  ------------------------------ **/

    fun createBoard(token: String, name: String, description: String): Int { /** check **/
        isValidString(name)
        isValidString(description)
        if (boardData.getBoardByName(name) != null) throw TrelloException.AlreadyExists(name)
        val idUser = getUser(token).idUser /**if I don't do it like this, we will have boardData with its own getUser -_-**/
        return boardData.createBoard(idUser, name, description)
    }

    fun getBoard(token: String, idBoard: Int): Board { /** check **/
        val idUser = getUser(token).idUser
        if (!checkUserInBoard(idUser, idBoard)) throw TrelloException.NotFound("Board")
        return boardData.getBoard(idBoard) ?: throw TrelloException.NotFound("Board")
    }

    fun getBoardsFromUser(token: String): List<Board> { /** check **/
        val idUser = getUser(token).idUser
        return boardData.getBoardsFromUser(idUser)
    }

    fun addUserToBoard(token: String, idUser: Int, idBoard: Int) { /** check **/
        val newIdUser = getUser(idUser).idUser // user to add
        if (checkUserInBoard(newIdUser, idBoard)) return // Idempotent Operation => "PUT"
        boardData.addUserToBoard(idUser,idBoard)
    }
}