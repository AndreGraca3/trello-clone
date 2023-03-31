package pt.isel.ls.server.services

import pt.isel.ls.server.data.dataInterfaces.CardData
import pt.isel.ls.server.data.checkListInBoard
import pt.isel.ls.server.data.checkUserInBoard
import pt.isel.ls.server.data.getUser
import pt.isel.ls.server.utils.Card
import pt.isel.ls.server.utils.checkEndDate
import pt.isel.ls.server.utils.isValidString

class CardServices(private val cardData: CardData) {

    /** -----------------------------  *
     *         Card Management         *
     *  ----------------------------- **/

    fun createCard(token: String, idBoard: Int, idList: Int, name: String, description: String, endDate: String?): Int {
        isValidString(name)
        isValidString(description)
        val idUser = getUser(token).idUser
        checkUserInBoard(idUser,idBoard)
        checkListInBoard(idList,idBoard)
        if (endDate != null) checkEndDate(endDate)
        return cardData.createCard(idList, idBoard, name, description, endDate)
    }

    fun getCard(token: String, idBoard: Int, idList: Int, idCard: Int): Card {
        val idUser = getUser(token).idUser
        checkUserInBoard(idUser,idBoard)
        checkListInBoard(idList,idBoard)
        return cardData.getCard(idCard,idList,idBoard)
    }

    fun getCardsFromList(token: String, idBoard: Int, idList: Int): List<Card> {
        val idUser = getUser(token).idUser
        checkUserInBoard(idUser,idBoard)
        checkListInBoard(idList,idBoard)
        return cardData.getCardsFromList(idList,idBoard)
    }

    fun moveCard(token: String, idBoard: Int, idListNow: Int, idListDst: Int, idCard: Int) {
        val idUser = getUser(token).idUser
        checkUserInBoard(idUser,idBoard)
        checkListInBoard(idListNow,idBoard)
        return cardData.moveCard(idCard, idListNow, idBoard, idListDst)
    }
}