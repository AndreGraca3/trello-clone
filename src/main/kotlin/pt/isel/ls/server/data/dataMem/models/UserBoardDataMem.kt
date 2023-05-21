package pt.isel.ls.server.data.dataMem.models

import pt.isel.ls.server.data.dataInterfaces.models.UserBoardData
import pt.isel.ls.server.data.dataMem.boards
import pt.isel.ls.server.data.dataMem.lists
import pt.isel.ls.server.data.dataMem.usersBoards
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.UserBoard
import java.sql.Connection

class UserBoardDataMem : UserBoardData {


    override fun addUserToBoard(idUser: Int, idBoard: Int, con: Connection) {
        usersBoards.add(UserBoard(idUser, idBoard))
    }

    override fun searchUserBoards(idUser: Int): List<Int> {
        return usersBoards.filter { it.idUser == idUser }.map { it.idBoard }
    }

    override fun checkUserInBoard(idUser: Int, idBoard: Int, con: Connection) {
        usersBoards.find { it.idUser == idUser && it.idBoard == idBoard } ?: throw TrelloException.NotFound("Board")
        /** If the board doesn't exist it makes sence returning not found,
         *  but if the board exists and the user doesn't belong to it, this should return Unauthorized.
         * **/
    }

    override fun getIdUsersFromBoard(idBoard: Int): List<Int> {
        return usersBoards.filter { it.idBoard == idBoard }.map { it.idUser }
    }

    override fun getBoardCountFromUser(idUser: Int, name: String, numLists: Int?): Int {
        usersBoards.filter { it.idUser == idUser }.forEach { userBoard ->
            return boards.filter { board ->
                board.idBoard == userBoard.idBoard &&
                        board.name == name &&
                        lists.count { it.idBoard == board.idBoard } == numLists
            }.size
        }
        return 0
    }

    override fun getUserCountFromBoard(idBoard: Int): Int {
        return usersBoards.count { it.idBoard == idBoard }
    }
}
