package pt.isel.ls.server.data.dataPostGres.statements

import pt.isel.ls.server.utils.Card
import java.time.LocalDate

object CardStatements {

    fun createCardCMD(idList: Int, idBoard: Int, name: String, description: String?, endDate: String?): String {
        return "INSERT INTO dbo.card (name, description, idList, idBoard, startDate, endDate) " +
                "VALUES ('$name', '$description', '$idList', '$idBoard', '${LocalDate.now()}', '$endDate');"
    }

    fun getCardsFromListCMD(idList: Int, idBoard: Int): String {
        return "SELECT * FROM dbo.card WHERE idList = $idList and idBoard = $idBoard;"
    }

    fun getCardCMD(idCard: Int, idList: Int, idBoard: Int): String {
        return "SELECT * FROM dbo.card WHERE idCard = $idCard and idList = $idList and idBoard = $idBoard;"
    }

    fun moveCardCMD(idCard: Int, idListNow: Int, idBoard: Int, idListDst: Int): String {
        return "UPDATE dbo.card SET idList = $idListDst " +
                "WHERE idCard = $idCard and idList = $idListNow and idBoard = $idBoard;"
    }
}
