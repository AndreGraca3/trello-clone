package pt.isel.ls.server.data.dataInterfaces

import pt.isel.ls.server.utils.User

interface UserBoardData {

    fun addUserToBoard(idUser: Int, idBoard: Int)

    fun searchUserBoards(idUser: Int): List<Int>

    fun checkUserInBoard(idUser: Int, idBoard: Int)

    fun getIdUsersFromBoard(idBoard: Int) : List<Int>
}
