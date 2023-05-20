package pt.isel.ls.server.data.dataPostGres.statements

object UserBoardStatements {

    fun addUserToBoard(idUser: Int, idBoard: Int): String {
        return "INSERT INTO dbo.user_board (idUser, idBoard) VALUES ($idUser,$idBoard);"
    }

    fun getBoardIdsFromUser(idUser: Int): String {
        return "SELECT idBoard FROM dbo.user_board where idUser = $idUser;"
    }

    fun checkUserInBoard(idUser: Int, idBoard: Int): String {
        return "SELECT * FROM dbo.user_board WHERE idUser = $idUser and idBoard = $idBoard;"
    }

    fun getIdUsersFromBoard(idBoard: Int): String {
        return "SELECT idUser FROM dbo.user_board where idBoard = $idBoard;"
    }

    fun getBoardCountFromUser(idUser: Int, name: String, numLists: Int): String {
        return "SELECT COUNT(*) AS count\n" +
                "FROM (SELECT DISTINCT b.idBoard\n" +
                "FROM dbo.board b\n" +
                "INNER JOIN dbo.user_board ub ON ub.idBoard = b.idBoard\n" +
                "LEFT JOIN dbo.list l ON l.idBoard = b.idBoard\n" +
                "WHERE ub.idUser = $idUser AND b.name LIKE '%$name%'\n" +
                "GROUP BY b.idBoard\n" +
                "HAVING COUNT(l.idList) >= $numLists\n" +
                ") AS subquery;"
    }

    fun getUserCountFromBoard(idBoard: Int): String {
        return "SELECT COUNT(idUser) FROM dbo.user_board where idBoard = $idBoard;"
    }
}
