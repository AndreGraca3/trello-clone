package pt.isel.ls.server.data.dataMem

import pt.isel.ls.server.data.dataInterfaces.UserBoardData
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.UserBoard

class UserBoardDataMem : UserBoardData {

    val usersBoards = mutableListOf<UserBoard>()

    override fun addUserToBoard(idUser: Int, idBoard: Int) {
        usersBoards.add(UserBoard(idUser, idBoard))
    }

    override fun searchUserBoards(idUser: Int): List<Int> {
        return usersBoards.filter { it.idUser == idUser }.map { it.idBoard }
    }

    override fun checkUserInBoard(idUser: Int, idBoard: Int) {
        usersBoards.find { it.idUser == idUser && it.idBoard == idBoard } ?: throw TrelloException.NotFound("Board")
    }
}
