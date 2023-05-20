package pt.isel.ls.server.data.dataPostGres.statements

object BoardStatements {

    fun createBoardCMD(name: String, description: String): String {
        return "INSERT INTO dbo.board (name, description) VALUES ('$name', '$description') RETURNING idBoard;"
    }

    fun getBoardCMD(idBoard: Int): String {
        return "SELECT b.name, b.description, l.idlist, l.name AS listName, c.idcard, c.name AS cardName, c.idx, c.archived FROM dbo.board b\n" +
                "INNER JOIN dbo.list l on l.idboard = b.idboard\n" +
                "LEFT OUTER JOIN dbo.card c ON c.idlist = l.idlist\n" +
                "WHERE b.idboard = $idBoard\n" +
                "ORDER BY l.idlist ASC, c.idcard ASC;"
    }

    fun getBoardsFromUser(idUser: Int, limit: Int?, skip: Int?, name: String, numLists: Int): String {
        return "SELECT b.idboard, b.name, b.description, COUNT(l.idlist) AS numLists\n" +
                "FROM dbo.board b\n" +
                "INNER JOIN dbo.user_board u ON u.idboard = b.idboard\n" +
                "LEFT OUTER JOIN dbo.list l ON l.idboard = b.idboard\n" +
                "WHERE u.iduser = $idUser AND b.name LIKE '%$name%'\n" +
                "GROUP BY b.idboard, b.name, b.description\n" +
                "HAVING COUNT(l.idlist) >= $numLists\n" +
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
