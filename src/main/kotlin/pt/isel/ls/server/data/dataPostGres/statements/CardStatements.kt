package pt.isel.ls.server.data.dataPostGres.statements

import java.time.LocalDate

object CardStatements {

    fun size(): String {
        return "SELECT COUNT(idCard) FROM dbo.card;"
    }

    fun createCardCMD(idList: Int, idBoard: Int, name: String, description: String, endDate: String?, idx: Int, archived: Boolean = false): String {
        return "INSERT INTO dbo.card (name, description, idList, idBoard, startDate, endDate, archived, idx) " +
                "VALUES ('$name', '$description', $idList, $idBoard, '${LocalDate.now()}', $endDate, $archived, $idx) RETURNING idCard;"
    }

    fun getCardsFromListCMD(idList: Int, idBoard: Int, limit: Int, skip: Int): String {
        return "SELECT * FROM dbo.card WHERE idList = $idList and idBoard = $idBoard LIMIT $limit OFFSET $skip;"
    }

    fun getCardCMD(idCard: Int, idList: Int, idBoard: Int): String {
        return "SELECT * FROM dbo.card WHERE idCard = $idCard and idList = $idList and idBoard = $idBoard;"
    }

    fun moveCardCMD(idCard: Int, idListNow: Int, idBoard: Int, idListDst: Int, idxDst: Int): String {
        return "UPDATE dbo.card SET idList = $idListDst, idx = $idxDst " +
               "WHERE idCard = $idCard and idList = $idListNow and idBoard = $idBoard;"
    }





    fun deleteCard(idCard: Int, idList: Int, idBoard: Int): String {
        return "DELETE FROM dbo.card where idCard = $idCard and idList = $idList and idBoard = $idBoard RETURNING idx;"
    }

    fun getNextIdx(idList: Int): String {
        return "SELECT max(idx) FROM dbo.card where idList = $idList;"
    }

    fun getCardCount(idBoard: Int, idList: Int): String {
        return "SELECT COUNT(idCard) FROM dbo.card where idBoard = $idBoard and idList = $idList;"
    }

    fun decreaseIdx(idList: Int, idx: Int): String {
        return "UPDATE dbo.card SET idx = idx - 1 WHERE idList = $idList and idx >= $idx;"
    }

    fun increaseIdx(idList: Int, idx: Int): String {
        return "UPDATE dbo.card SET idx = idx + 1 WHERE idList = $idList and idx >= $idx;"
    }
}
