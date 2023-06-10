package pt.isel.ls.server.data.dataPostGres.statements

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object CardStatements {

    fun size(): String {
        return "SELECT COUNT(idCard) FROM dbo.card;"
    }

    fun createCardCMD(
        idList: Int,
        idBoard: Int,
        name: String,
        description: String?,
        endDate: String?,
        idx: Int,
        archived: Boolean = false
    ): String {
        return "INSERT INTO dbo.card (name, description, idList, idBoard, startDate, endDate, archived, idx) " +
            "VALUES ('$name', ${if (description != null) "'$description'" else null}, $idList, $idBoard, '${
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
            }', $endDate, $archived, $idx) RETURNING idCard;"
    }

    fun getCardsFromListCMD(idList: Int, idBoard: Int): String {
        return "SELECT * FROM dbo.card WHERE idList = $idList and idBoard = $idBoard ORDER BY idx ASC;"
    }

    fun getCardCMD(idCard: Int, idBoard: Int): String {
        // return "SELECT * FROM dbo.card WHERE idCard = $idCard and idList = $idList and idBoard = $idBoard;"
        return "SELECT * FROM dbo.card WHERE idCard = $idCard and idBoard = $idBoard;"
    }

    fun moveCardCMD(idCard: Int, idListNow: Int, idBoard: Int, idListDst: Int, idxDst: Int): String {
        return "UPDATE dbo.card SET idList = $idListDst, idx = $idxDst " +
            "WHERE idCard = $idCard and idList = $idListNow and idBoard = $idBoard;"
    }

    fun deleteCard(idCard: Int, idBoard: Int): String {
        return "DELETE FROM dbo.card where idCard = $idCard and idBoard = $idBoard RETURNING idx, idList;"
    }

    fun deleteCards(idList: Int): String {
        return "DELETE FROM dbo.card WHERE idList = $idList;"
    }

    fun archiveCards(idBoard: Int, idList: Int): String {
        return "UPDATE dbo.card SET idList = null, archived = true WHERE idList = $idList and idBoard = $idBoard;"
    }

    fun getNextIdx(idList: Int): String {
        return "SELECT max(idx) FROM dbo.card where idList = $idList;"
    }

    fun getCardCount(idBoard: Int, idList: Int): String {
        return "SELECT COUNT(idCard) FROM dbo.card where idBoard = $idBoard and idList = $idList;"
    }

    fun getArchivedCards(idBoard: Int): String {
        return "SELECT * FROM dbo.card where card.idList is null and card.idBoard = $idBoard;"
    }

    fun decreaseIdx(idList: Int?, idx: Int): String {
        return "UPDATE dbo.card SET idx = idx - 1 WHERE idList = $idList and idx >= $idx;"
    }

    fun increaseIdx(idList: Int, idx: Int): String {
        return "UPDATE dbo.card SET idx = idx + 1 WHERE idList = $idList and idx >= $idx;"
    }

    fun updateCard(idCard: Int, idBoard: Int, description: String?, endDate: String?, idList: Int?, archived: Boolean, idx: Int): String {
        return "UPDATE dbo.card " +
            "SET description = ${if (description != null) "'$description'" else null}, " +
            "endDate = ${if (endDate != null) "'$endDate'" else null}, " +
            "idList = $idList, archived = $archived, idx = $idx " +
            "where idCard = $idCard and idBoard = $idBoard;"
    }
}
