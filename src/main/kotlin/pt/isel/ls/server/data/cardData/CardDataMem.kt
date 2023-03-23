package pt.isel.ls.server.data.cardData

import pt.isel.ls.Board
import pt.isel.ls.Card
import pt.isel.ls.server.getNextId
import java.time.LocalDate

class CardDataMem : IDataCard {
    private val cards = mutableListOf<Card>()

    override fun createCard(idList: Int, name: String, description: String, endDate: String?): Int {
        val newCard =
            Card(getNextId(cards), idList, name, description, LocalDate.now().toString(), endDate, false)
        cards.add(newCard)
        return newCard.idCard
    }

    override fun getCardsFromList(idList: Int): List<Card> {
        return cards.filter { it.idList == idList }
    }

    override fun getCard(idCard: Int): Card? {
        return cards.find { it.idCard == idCard }
    }

    override fun moveCard(card: Card, idListDst: Int) {
        card.idList = idListDst
    }
}
