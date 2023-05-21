package pt.isel.ls.server.services

import pt.isel.ls.server.data.dataInterfaces.DataExecutor
import pt.isel.ls.server.data.dataInterfaces.models.CardData
import pt.isel.ls.server.data.dataInterfaces.models.ListData
import pt.isel.ls.server.data.dataInterfaces.models.UserBoardData
import pt.isel.ls.server.data.dataInterfaces.models.UserData
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.exceptions.map
import pt.isel.ls.server.utils.Card
import pt.isel.ls.server.utils.checkEndDate
import pt.isel.ls.server.utils.isValidString
import java.sql.SQLException

class CardServices(
    private val userData: UserData,
    private val userBoardData: UserBoardData,
    private val listData: ListData,
    private val cardData: CardData,
    private val dataExecutor: DataExecutor<Any>
) {

    fun createCard(
        token: String,
        idBoard: Int,
        idList: Int,
        name: String,
        description: String?,
        endDate: String?
    ): Int {
        isValidString(name, "name")
        if (description != null) isValidString(description, "description")

        return dataExecutor.execute {
            val idUser = userData.getUser(token, it).idUser
            userBoardData.checkUserInBoard(idUser, idBoard, it)
            listData.getList(idList, idBoard, it)
            if (endDate != null) checkEndDate(endDate)
            cardData.createCard(idList, idBoard, name, description, endDate, it)
        } as Int
    }

    fun getCard(token: String, idBoard: Int, idCard: Int): Card {
        return dataExecutor.execute {
            val idUser = userData.getUser(token, it).idUser
            userBoardData.checkUserInBoard(idUser, idBoard, it)
            cardData.getCard(idCard, idBoard, it)
        } as Card
    }

    fun moveCard(token: String, idBoard: Int, idListNow: Int, idListDst: Int, idCard: Int, idxDst: Int) {
        return dataExecutor.execute {
            val idUser = userData.getUser(token, it).idUser
            userBoardData.checkUserInBoard(idUser, idBoard, it)
            listData.getList(idListNow, idBoard, it)
            listData.getList(idListDst, idBoard, it)
            val card = cardData.getCard(idCard, idBoard, it)
            if (idxDst !in 1..cardData.getNextIdx(idListDst, it)) throw TrelloException.IllegalArgument("idx")
            cardData.moveCard(idCard, idBoard, idListNow, idListDst, card.idx, idxDst, it)
        } as Unit
    }

    fun deleteCard(token: String, idBoard: Int, idCard: Int) {
        return dataExecutor.execute {
            val idUser = userData.getUser(token, it).idUser
            userBoardData.checkUserInBoard(idUser, idBoard, it)
            cardData.deleteCard(idCard, idBoard, it)
        } as Unit
    }

    fun updateCard(token: String, idBoard: Int, idCard: Int, archived: Boolean, description: String, endDate: String) {
        return dataExecutor.execute {
            val idUser = userData.getUser(token, it).idUser
            userBoardData.checkUserInBoard(idUser, idBoard, it)
            val card = cardData.getCard(idCard, idBoard, it)
            val newDesc = if (description == "") card.description else description
            val newEndDate = if (endDate == "") null else endDate
            cardData.updateCard(card, archived, newDesc!!, newEndDate, it)
        } as Unit
    }
}
