package pt.isel.ls.server.data.dataInterfaces.models

import java.sql.Connection

interface UserBoardData {

    fun addUserToBoard(idUser: Int, idBoard: Int, con: Connection)

    fun searchUserBoards(idUser: Int): List<Int>

    fun checkUserInBoard(idUser: Int, idBoard: Int, con: Connection)

    fun getIdUsersFromBoard(idBoard: Int): List<Int>

    fun getBoardCountFromUser(idUser: Int, name: String, numLists: Int?): Int

    fun getUserCountFromBoard(idBoard: Int): Int
}
