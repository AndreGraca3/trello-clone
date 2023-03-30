package pt.isel.ls.server.services

import pt.isel.ls.server.data.cardData.DataCard
import pt.isel.ls.server.data.checkCardInList
import pt.isel.ls.server.data.checkListInBoard
import pt.isel.ls.server.data.checkUserInBoard
import pt.isel.ls.server.data.getUser
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.Card
import pt.isel.ls.server.utils.checkEndDate
import pt.isel.ls.server.utils.isValidString

class CardServices(private val cardData: DataCard) {

    /** -----------------------------  *
     *         Card Management         *
     *  ----------------------------- **/

    fun createCard(token: String, idBoard: Int, idList: Int, name: String, description: String, endDate: String?): Int { /** check **/
        isValidString(name)
        isValidString(description)
        val idUser = getUser(token).idUser
        if(!checkUserInBoard(idUser,idBoard)) throw TrelloException.NotAuthorized()
        if(!checkListInBoard(idList,idBoard)) throw TrelloException.NotAuthorized()
        if (endDate != null) checkEndDate(endDate)
        return cardData.createCard(idList, idBoard, name, description, endDate)
    }

    fun getCard(token: String, idBoard: Int, idList: Int, idCard: Int): Card { /** check **/
        val idUser = getUser(token).idUser
        if(!checkUserInBoard(idUser,idBoard)) throw TrelloException.NotAuthorized()
        if(!checkListInBoard(idList,idBoard)) throw TrelloException.NotAuthorized()
        return cardData.getCard(idCard,idList,idBoard) ?: throw TrelloException.NotFound("Card")
    }

    fun getCardsFromList(token: String, idBoard: Int, idList: Int): List<Card> {
        getList(token,idBoard, idList)
        return cardData.getCardsFromList(idList)
    }

    fun moveCard(token: String, idBoard: Int, idListNow: Int, idListDst: Int, idCard: Int) {
        val card = getCard(token, idBoard, idListNow, idCard) // verifies that card "belongs" to the user.
        getList(token, idBoard, idListDst) // verifies that listDst "belongs" to the user.
        return cardData.moveCard(card, idListDst)
    }
}