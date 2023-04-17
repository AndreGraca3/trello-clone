package pt.isel.ls.server.data.dataMem

import pt.isel.ls.server.data.dataInterfaces.UserBoardData
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.User
import pt.isel.ls.server.utils.UserBoard

class UserBoardDataMem : UserBoardData {

    val usersBoards = mutableListOf<UserBoard>(UserBoard(1,0), UserBoard(1,1))

    override fun addUserToBoard(idUser: Int, idBoard: Int) {
        usersBoards.add(UserBoard(idUser, idBoard))
    }

    override fun searchUserBoards(idUser: Int): List<Int> {
        return usersBoards.filter { it.idUser == idUser }.map { it.idBoard }
    }

    override fun checkUserInBoard(idUser: Int, idBoard: Int) {
        usersBoards.find { it.idUser == idUser && it.idBoard == idBoard } ?: throw TrelloException.NotFound("Board")
        /** If the board doesn't exist it makes sence returning not found,
         *  but if the board exists and the user doesn't belong to it, this should return Unauthorized.
         * **/
    }

    override fun getIdUsersFromBoard(idBoard: Int): List<Int> {
        return usersBoards.filter { it.idBoard == idBoard }.map { it.idUser }
    }

    override fun getBoardCountFromUser(idUser: Int): Int {
        return usersBoards.count { it.idUser == idUser }
    }

    override fun getUserCountFromBoard(idBoard: Int): Int {
        return usersBoards.count { it.idBoard == idBoard}
    }
}
