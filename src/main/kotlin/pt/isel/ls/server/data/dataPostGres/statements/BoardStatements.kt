package pt.isel.ls.server.data.dataPostGres.statements

object BoardStatements {

    fun createBoardCMD(name: String, description: String, primaryColor: String, secondaryColor: String): String {
        return "INSERT INTO dbo.board (name, description, primaryColor, secondaryColor) " +
            "VALUES ('$name', '$description', '$primaryColor', '$secondaryColor') RETURNING idBoard;"
    }

    fun getBoardCMD(idBoard: Int): String {
        return "SELECT *\n" +
            "FROM dbo.board\n" +
            "WHERE idboard = $idBoard;"
    }

    fun getBoardsFromUser(idUser: Int, limit: Int?, skip: Int?, name: String, numLists: Int?): String {
        return "SELECT b.idboard, b.name, b.description, b.primaryColor, b.secondaryColor, COUNT(l.idlist) AS numLists\n" +
            "FROM dbo.board b\n" +
            "INNER JOIN dbo.user_board u ON u.idboard = b.idboard\n" +
            "LEFT OUTER JOIN dbo.list l ON l.idboard = b.idboard\n" +
            "WHERE u.iduser = $idUser AND b.name LIKE '%$name%'\n" +
            "GROUP BY b.idboard, b.name, b.description\n" +
            "HAVING COUNT(l.idlist) = coalesce($numLists,count(l.idlist))\n" +
            "ORDER BY b.idboard ASC\n" +
            "LIMIT $limit OFFSET $skip;"
    }

    fun checkUserInBoard(idUser: Int, idBoard: Int): String {
        return "SELECT * FROM dbo.user_board WHERE idUser = $idUser and idBoard = $idBoard;"
    }

    fun size(): String {
        return "SELECT COUNT(idBoard) FROM dbo.board;"
    }
}
