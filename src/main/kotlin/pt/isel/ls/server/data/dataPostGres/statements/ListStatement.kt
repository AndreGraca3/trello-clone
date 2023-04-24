package pt.isel.ls.server.data.dataPostGres.statements

object ListStatement {

    fun size(): String {
        return "SELECT COUNT(idList) FROM dbo.list;"
    }

    fun createListCMD(idBoard: Int, name: String): String {
        return "INSERT INTO dbo.list (idBoard, name) VALUES ($idBoard, '$name') RETURNING idList;"
    }

    fun getListCMD(idList: Int, idBoard: Int): String {
        return "SELECT * FROM dbo.list where idList = $idList and idBoard = $idBoard;"
    }

    fun getListOfBoard(idBoard: Int, limit: Int, skip: Int): String {
        return "SELECT * FROM dbo.list where idBoard = $idBoard LIMIT $limit OFFSET $skip;"
    }

    fun checkListInBoard(idList: Int, idBoard: Int): String {
        return "SELECT * FROM dbo.list where idBoard = $idBoard and idList = $idList;"
    }

    fun deleteList(idList: Int, idBoard: Int): String {
        return "DELETE FROM dbo.list where idList = $idList and idBoard = $idBoard RETURNING idList;"
    }

    fun getListCount(idBoard: Int): String {
        return "SELECT COUNT(idList) FROM dbo.list where idBoard = $idBoard;"
    }
}
