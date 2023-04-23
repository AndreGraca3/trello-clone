package pt.isel.ls.server.data.dataPostGres.statements

object UserBoardStatements {

    fun getBoardIdsFromUser(idUser: Int): String {
        return "SELECT idBoard FROM dbo.user_board where idUser = $idUser;"
    }

    fun checkUserInBoard(idUser: Int, idBoard: Int): String {
        return "SELECT * FROM dbo.user_board WHERE idUser = $idUser and idBoard = $idBoard;"
    }

    fun getIdUsersFromBoard(idBoard: Int): String {
        return "SELECT idUser FROM dbo.user_board where idBoard = $idBoard;"
    }

    fun getBoardCountFromUser(idUser: Int): String {
        return "SELECT COUNT(idBoard) FROM dbo.user_board where idUser = $idUser;"
    }

    fun getUserCountFromBoard(idBoard: Int): String {
        return "SELECT COUNT(idUser) FROM dbo.user_board where idBoard = $idBoard;"
    }

}