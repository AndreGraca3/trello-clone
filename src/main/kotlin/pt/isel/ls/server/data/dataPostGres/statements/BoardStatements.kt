package pt.isel.ls.server.data.dataPostGres.statements

object BoardStatements {

    fun createBoardCMD(name: String, description: String): String {
        return "INSERT INTO dbo.board (name, description) VALUES ('$name', '$description');"
    }

    fun getBoardCMD(idBoard: Int): String {
        return "SELECT * FROM dbo.board WHERE idBoard = $idBoard;"
    }

    fun getBoardByNameCMD(name: String): String {
        return "SELECT * FROM dbo.board WHERE name = '$name';"
    }

    fun getBoardsFromUser(idUser: Int): String {
        return "SELECT * FROM dbo.board b, dbo.user_board u WHERE u.idUser = '$idUser' and b.idBoard = u.idBoard;"
    }

    fun addUserToBoard(idUser: Int, idBoard: Int): String {
        return "INSERT INTO dbo.user_board (idUser, idBoard) VALUES ($idUser,$idBoard);"
    }

    fun checkUserInBoard(idUser: Int, idBoard: Int): String {
        return "SELECT * FROM dbo.user_board WHERE idUser = $idUser and idBoard = $idBoard;"
    }
}
