package pt.isel.ls.server.services

import pt.isel.ls.server.data.dataInterfaces.CardData
import pt.isel.ls.server.data.dataInterfaces.ListData
import pt.isel.ls.server.data.dataInterfaces.UserBoardData
import pt.isel.ls.server.data.dataInterfaces.UserData
import pt.isel.ls.server.utils.Card
import pt.isel.ls.server.utils.checkEndDate
import pt.isel.ls.server.utils.isValidString

class CardServices(
    private val userData: UserData,
    private val userBoardData: UserBoardData,
    private val listData: ListData,
    private val cardData: CardData
) {

    /** -----------------------------  *
     *         Card Management         *
     *  ----------------------------- **/

    fun createCard(token: String, idBoard: Int, idList: Int, name: String, description: String, endDate: String?): Int {
        isValidString(name)
        isValidString(description)
        val idUser = userData.getUser(token).idUser
        userBoardData.checkUserInBoard(idUser, idBoard)
        listData.checkListInBoard(idList, idBoard)
        if (endDate != null) checkEndDate(endDate)
        return cardData.createCard(idList, idBoard, name, description, endDate)
    }

    fun getCard(token: String, idBoard: Int, idList: Int, idCard: Int): Card {
        val idUser = userData.getUser(token).idUser
        userBoardData.checkUserInBoard(idUser, idBoard)
        listData.checkListInBoard(idList, idBoard)
        return cardData.getCard(idCard, idList, idBoard)
    }

    fun getCardsFromList(token: String, idBoard: Int, idList: Int): List<Card> {
        val idUser = userData.getUser(token).idUser
        userBoardData.checkUserInBoard(idUser, idBoard)
        listData.checkListInBoard(idList, idBoard)
        return cardData.getCardsFromList(idList, idBoard)
    }

    fun moveCard(token: String, idBoard: Int, idListNow: Int, idListDst: Int, idCard: Int) {
        val idUser = userData.getUser(token).idUser
        userBoardData.checkUserInBoard(idUser, idBoard)
        listData.checkListInBoard(idListNow, idBoard)
        return cardData.moveCard(idCard, idListNow, idBoard, idListDst)
    }
}
