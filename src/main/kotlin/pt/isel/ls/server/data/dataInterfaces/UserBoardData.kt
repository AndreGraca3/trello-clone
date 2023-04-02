package pt.isel.ls.server.data.dataInterfaces

interface UserBoardData {

    fun addUserToBoard(idUser: Int, idBoard: Int)

    fun searchUserBoards(idUser: Int): List<Int>

    fun checkUserInBoard(idUser: Int, idBoard: Int)
}
