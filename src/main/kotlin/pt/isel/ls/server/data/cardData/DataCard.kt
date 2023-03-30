package pt.isel.ls.server.data.cardData

import pt.isel.ls.server.utils.Card
import pt.isel.ls.server.data.cards
import pt.isel.ls.server.data.getNextId
import java.time.LocalDate

class DataCard : IDataCard {
    override fun createCard(idList: Int, idBoard: Int, name: String, description: String, endDate: String?): Int {
        val newCard =
            Card(getNextId(Card::class.java), idList, idBoard, name, description, LocalDate.now().toString(), endDate, false)
        cards.add(newCard)
        return newCard.idCard
    }

    override fun getCardsFromList(idList: Int): List<Card> {
        return cards.filter { it.idList == idList }
    }

    override fun getCard(idCard: Int, idList: Int, idBoard: Int): Card? {
        return cards.find { it.idCard == idCard && it.idList == idList && it.idBoard == idBoard }
    }

    override fun moveCard(card: Card, idListDst: Int) {
        card.idList = idListDst
    }
}
