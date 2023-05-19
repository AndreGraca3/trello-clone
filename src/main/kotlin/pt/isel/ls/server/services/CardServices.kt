package pt.isel.ls.server.services

import pt.isel.ls.server.data.dataInterfaces.CardData
import pt.isel.ls.server.data.dataInterfaces.ListData
import pt.isel.ls.server.data.dataInterfaces.UserBoardData
import pt.isel.ls.server.data.dataInterfaces.UserData
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.exceptions.map
import pt.isel.ls.server.utils.Card
import pt.isel.ls.server.utils.checkEndDate
import pt.isel.ls.server.utils.checkPaging
import pt.isel.ls.server.utils.isValidString
import java.sql.SQLException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CardServices(
    private val userData: UserData,
    private val userBoardData: UserBoardData,
    private val listData: ListData,
    private val cardData: CardData
) {

    /** -----------------------------  *
     *         Card Management         *
     *  ----------------------------- **/

    fun createCard(token: String, idBoard: Int, idList: Int, name: String, description: String?, endDate: String?): Int {
        isValidString(name, "name")
        if (description != null) isValidString(description, "description")
        val idUser = userData.getUser(token).idUser
        userBoardData.checkUserInBoard(idUser, idBoard)
        //listData.getList(idList, idBoard)
        if (endDate != null) checkEndDate(endDate)
        try{
            return cardData.createCard(idList, idBoard, name, description, endDate)
        } catch (ex: SQLException) {
            val trelloException = map[ex.sqlState] ?: throw Exception()
            throw trelloException("List")
        }
    }

    fun getCard(token: String, idBoard: Int, idCard: Int): Card {
        val idUser = userData.getUser(token).idUser
        userBoardData.checkUserInBoard(idUser, idBoard)
        //listData.getList(idList, idBoard)
        return cardData.getCard(idCard, idBoard)
    }

    fun moveCard(token: String, idBoard: Int, idListNow: Int, idListDst: Int, idCard: Int, idxDst: Int) {
        val idUser = userData.getUser(token).idUser
        userBoardData.checkUserInBoard(idUser, idBoard)
        listData.getList(idListNow, idBoard)
        listData.getList(idListDst, idBoard)
        if (idxDst !in 1..cardData.getNextIdx(idListDst)) throw TrelloException.IllegalArgument("idx")
        cardData.moveCard(idCard, idBoard, idListDst, idxDst)
    }

    fun deleteCard(token: String, idBoard: Int, idCard: Int) {
        val idUser = userData.getUser(token).idUser
        userBoardData.checkUserInBoard(idUser, idBoard)
        //listData.getList(idList, idBoard)
        return cardData.deleteCard(idCard, idBoard)
    }

    fun updateCard(token: String, idBoard: Int, idCard: Int, archived: Boolean, description: String, endDate: String) {
        val idUser = userData.getUser(token).idUser
        userBoardData.checkUserInBoard(idUser, idBoard)
        //listData.getList(idList, idBoard)
        val card = cardData.getCard(idCard, idBoard)
        val newDesc = if(description == "") card.description else description
        val newEndDate = if(endDate == "") null else endDate
        return cardData.updateCard(card, archived, newDesc!!, newEndDate)
    }
}
