package pt.isel.ls.server.services

import pt.isel.ls.server.Card
import pt.isel.ls.server.data.dataInterfaces.models.CardData
import pt.isel.ls.server.data.dataInterfaces.models.ListData
import pt.isel.ls.server.data.dataInterfaces.models.UserBoardData
import pt.isel.ls.server.data.dataInterfaces.models.UserData
import pt.isel.ls.server.data.transactionManager.executor.DataExecutor
import pt.isel.ls.server.exceptions.INVAL_PARAM
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.checkEndDate
import pt.isel.ls.server.utils.validateString

class CardServices(
    private val userData: UserData,
    private val userBoardData: UserBoardData,
    private val listData: ListData,
    private val cardData: CardData,
    private val dataExecutor: DataExecutor
) {

    fun createCard(
        token: String,
        idBoard: Int,
        idList: Int,
        name: String,
        description: String?,
        endDate: String?
    ): Int {
        validateString(name, "name")
        if (description != null) validateString(description, "description")

        return dataExecutor.execute {
            val idUser = userData.getUser(token, it).idUser
            userBoardData.checkUserInBoard(idUser, idBoard, it)
            listData.getList(idList, idBoard, it)
            if (endDate != null && !checkEndDate(endDate)) {
                throw TrelloException.IllegalArgument("$INVAL_PARAM $endDate")
            }
            cardData.createCard(idList, idBoard, name, description, endDate, it)
        }
    }

    fun getCard(token: String, idBoard: Int, idCard: Int): Card {
        return dataExecutor.execute {
            val idUser = userData.getUser(token, it).idUser
            userBoardData.checkUserInBoard(idUser, idBoard, it)
            cardData.getCard(idCard, idBoard, it)
        }
    }

    fun moveCard(token: String, idBoard: Int, idListNow: Int, idListDst: Int, idCard: Int, idxDst: Int) {
        return dataExecutor.execute {
            val idUser = userData.getUser(token, it).idUser
            userBoardData.checkUserInBoard(idUser, idBoard, it)
            listData.getList(idListNow, idBoard, it)
            listData.getList(idListDst, idBoard, it)
            val card = cardData.getCard(idCard, idBoard, it)
            if (idxDst !in 1..cardData.getNextIdx(
                    idListDst,
                    it
                )
            ) {
                throw TrelloException.IllegalArgument("$INVAL_PARAM idx")
            }
            cardData.moveCard(idCard, idBoard, idListNow, idListDst, card.idx, idxDst, it)
        }
    }

    fun deleteCard(token: String, idBoard: Int, idCard: Int) {
        return dataExecutor.execute {
            val idUser = userData.getUser(token, it).idUser
            userBoardData.checkUserInBoard(idUser, idBoard, it)
            cardData.deleteCard(idCard, idBoard, it)
        }
    }

    fun updateCard(
        token: String,
        idBoard: Int,
        idCard: Int,
        description: String?,
        endDate: String,
        idList: Int?,
        archived: Boolean
    ) {
        return dataExecutor.execute {
            val idUser = userData.getUser(token, it).idUser
            userBoardData.checkUserInBoard(idUser, idBoard, it)
            val card = cardData.getCard(idCard, idBoard, it)
            if (card.idList != null && idList != null && card.idList != idList) throw TrelloException.IllegalArgument("$INVAL_PARAM idList")
            val newEndDate = if (endDate == "") null else endDate
            val idx = if (card.idList == null && idList != null) cardData.getNextIdx(idList, it) else card.idx
            cardData.updateCard(card, description, newEndDate, idList, archived, idx, it)
        }
    }
}
