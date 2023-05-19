package pt.isel.ls.server.data.dataPostGres.statements

object BoardStatements {

    fun createBoardCMD(name: String, description: String): String {
        return "INSERT INTO dbo.board (name, description) VALUES ('$name', '$description') RETURNING idBoard;"
    }

    fun getBoardCMD(idBoard: Int): String {
        //return "SELECT * FROM dbo.board WHERE idBoard = $idBoard;"
        return "SELECT b.name, b.description, l.idlist, l.name AS listName, c.idcard, c.name AS cardName, c.idx, c.archived FROM dbo.board b\n" +
                "INNER JOIN dbo.list l on l.idboard = b.idboard\n" +
                "LEFT OUTER JOIN dbo.card c ON c.idlist = l.idlist\n" +
                "WHERE b.idboard = $idBoard\n" +
                "ORDER BY l.idlist ASC, c.idcard ASC;"
    }

    fun getBoardByNameCMD(name: String): String {
        return "SELECT * FROM dbo.board WHERE name = '$name';"
    }

    fun getBoardsFromUser(idUser: Int, limit: Int?, skip: Int?): String {
        //return "SELECT * FROM dbo.board where idBoard IN $idBoardsString LIMIT $limit OFFSET $skip;"
        return "SELECT b.idboard, b.name, b.description, COUNT(l.idlist) AS numLists\n" +
                "FROM dbo.board b\n" +
                "INNER JOIN dbo.user_board u on u.idboard = b.idboard\n" +
                "left outer join dbo.list l on l.idboard = b.idboard\n" +
                "where u.iduser = $idUser\n" +
                "group by b.idboard\n" +
                "order by b.idboard ASC\n" +
                "LIMIT $limit OFFSET $skip;"
    }

    fun checkUserInBoard(idUser: Int, idBoard: Int): String {
        return "SELECT * FROM dbo.user_board WHERE idUser = $idUser and idBoard = $idBoard;"
    }

    fun size(): String {
        return "SELECT COUNT(idBoard) FROM dbo.board;"
    }
}
