package pt.isel.ls.server.data.dataInterfaces

import pt.isel.ls.server.utils.Card

interface CardData {
    fun createCard(
        idList: Int,
        idBoard: Int,
        name: String,
        description: String,
        endDate: String? = null
    ): Int // check endDate

    fun getCardsFromList(idList: Int, idBoard: Int): List<Card>

    fun getCard(idCard: Int, idList: Int, idBoard: Int): Card

    fun moveCard(idCard: Int, idListNow: Int, idBoard: Int, idListDst: Int, idxDst: Int)

    fun deleteCard(idCard: Int, idList: Int, idBoard: Int)

    fun getNextIdx(idList: Int): Int
}
