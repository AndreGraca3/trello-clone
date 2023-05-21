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

    fun getListsOfBoard(idBoard: Int): String {
        return "SELECT * FROM dbo.list where idBoard = $idBoard;"
    }

    fun deleteList(idList: Int, idBoard: Int): String {
        return "DELETE FROM dbo.list where idList = $idList and idBoard = $idBoard;"
    }

    fun getListCount(idBoard: Int): String {
        return "SELECT COUNT(idList) FROM dbo.list where idBoard = $idBoard;"
    }
}
