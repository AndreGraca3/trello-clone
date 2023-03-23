package pt.isel.ls.server.services

import pt.isel.ls.Card
import pt.isel.ls.server.checkEndDate
import pt.isel.ls.server.data.cardData.IDataCard
import pt.isel.ls.server.data.listData.IDataList
import pt.isel.ls.server.data.userData.IUserData
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.isValidString

class CardServices(private val listServices: ListServices, private val cardData: IDataCard) {

    fun createCard(token: String, idList: Int, name: String, description: String, endDate: String?): Int {
        isValidString(name)
        isValidString(description)
        if (endDate != null) checkEndDate(endDate)
        listServices.getList(token, idList)
        return cardData.createCard(idList, name, description, endDate)
    }

    fun getCard(token: String, idCard: Int): Card {
        val card = cardData.getCard(idCard) ?: throw TrelloException.NotFound("Card")
        listServices.getList(token, card.idList)
        return card
    }

    fun getCardsFromList(token: String, idList: Int): List<Card> {
        listServices.getList(token, idList)
        return cardData.getCardsFromList(idList)
    }

    fun moveCard(token: String, idCard: Int, idList: Int) {
        val card = getCard(token, idCard) // verifies that card "belongs" to the user.
        listServices.getList(token, idList) // verifies that listDst "belongs" to the user.
        return cardData.moveCard(card, idList)
    }
}