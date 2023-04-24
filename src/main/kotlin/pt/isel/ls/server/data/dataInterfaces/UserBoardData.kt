package pt.isel.ls.server.data.dataInterfaces

interface UserBoardData {

    fun addUserToBoard(idUser: Int, idBoard: Int)

    fun searchUserBoards(idUser: Int): List<Int>

    fun checkUserInBoard(idUser: Int, idBoard: Int)

    fun getIdUsersFromBoard(idBoard: Int): List<Int>

    fun getBoardCountFromUser(idUser: Int): Int

    fun getUserCountFromBoard(idBoard: Int): Int
}
