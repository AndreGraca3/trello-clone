package pt.isel.ls.server.data.dataPostGres.dataSQL

import pt.isel.ls.server.data.dataInterfaces.UserBoardData

class UserBoardDataSQL : UserBoardData {

    override fun addUserToBoard(idUser: Int, idBoard: Int) {
        TODO("Not yet implemented")
    }

    override fun searchUserBoards(idUser: Int): List<Int> {
        TODO("Not yet implemented")
    }

    override fun checkUserInBoard(idUser: Int, idBoard: Int) {
        TODO("Not yet implemented")
    }

    override fun getIdUsersFromBoard(idBoard: Int): List<Int> {
        TODO("Not yet implemented")
    }

    override fun getBoardCountFromUser(idUser: Int): Int {
        TODO("Not yet implemented")
    }

    override fun getUserCountFromBoard(idBoard: Int): Int {
        TODO("Not yet implemented")
    }
}
