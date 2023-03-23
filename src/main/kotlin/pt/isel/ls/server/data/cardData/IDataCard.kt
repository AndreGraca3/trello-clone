package pt.isel.ls.server.data.cardData

import pt.isel.ls.Card

interface IDataCard {
    fun createCard(idList: Int, name: String, description: String, endDate: String? = null): Int // check endDate

    fun getCardsFromList(idList: Int): List<Card>

    fun getCard(idCard: Int): Card?

    fun moveCard(card: Card, idListDst: Int)
}
