package pt.isel.ls.server.data.dataMem

import pt.isel.ls.server.data.dataInterfaces.CardData
import pt.isel.ls.server.utils.Card
import pt.isel.ls.server.data.cards
import pt.isel.ls.server.data.getNextId
import pt.isel.ls.server.exceptions.TrelloException
import java.time.LocalDate

class CardDataMem : CardData {
    override fun createCard(idList: Int, idBoard: Int, name: String, description: String, endDate: String?): Int {
        val newCard =
            Card(
                getNextId(Card::class.java),
                idList,
                idBoard,
                name,
                description,
                LocalDate.now().toString(),
                endDate,
                false
            )
        cards.add(newCard)
        return newCard.idCard
    }

    override fun getCardsFromList(idList: Int, idBoard: Int): List<Card> {
        return cards.filter { it.idList == idList && it.idBoard == idBoard }
    }

    override fun getCard(idCard: Int, idList: Int, idBoard: Int): Card {
        return cards.find { it.idCard == idCard && it.idList == idList && it.idBoard == idBoard }
            ?: throw TrelloException.NotFound("Card")
    }

    override fun moveCard(idCard: Int, idListNow: Int, idBoard: Int, idListDst: Int) {
        val card = getCard(idCard, idListNow, idBoard)
        card.idList = idListDst
    }
}
