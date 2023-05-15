package pt.isel.ls.server.data.dataMem

import pt.isel.ls.server.data.dataInterfaces.CardData
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.Card
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class CardDataMem : CardData {

    val cards = mutableListOf<Card>()

    override val size get() = cards.size

    override fun createCard(idList: Int, idBoard: Int, name: String, description: String?, endDate: String?): Int {
        val newCard =
            Card(
                getNextId(),
                idList,
                idBoard,
                name,
                description,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                endDate,
                false,
                getNextIdx(idList)
            )
        cards.add(newCard)
        return newCard.idCard
    }

    override fun getCardsFromList(idList: Int, idBoard: Int, limit: Int, skip: Int): List<Card> {
        return cards.filter { it.idList == idList && it.idBoard == idBoard }.subList(skip, skip + limit).sortedBy { it.idx }
    }

    override fun getCard(idCard: Int, idBoard: Int): Card {
        return cards.find { it.idCard == idCard &&  it.idBoard == idBoard }
            ?: throw TrelloException.NotFound("Card")
    }

    override fun moveCard(idCard: Int, idBoard: Int, idListDst: Int, idxDst: Int) {
        val card = getCard(idCard, idBoard)
        val cardIdx = card.idx
        cards.forEach {
            if (it.idList == card.idList && it.idx >= cardIdx) it.idx--
            if (it.idList == idListDst && it.idx >= idxDst) it.idx++
        }
        card.idList = idListDst
        card.idx = idxDst
    }

    override fun deleteCard(idCard: Int, idBoard: Int) {
        val card: Card
        try {
            card = getCard(idCard, idBoard)
        } catch (ex: TrelloException) {
            throw TrelloException.NoContent("card")
        }
        cards.remove(card)
        if(card.idList == null) return
        cards.forEach {
            if (it.idList == card.idList && it.idx >= card.idx) it.idx--
        }
    }

    override fun getNextIdx(idList: Int): Int {
        val filtered = cards.filter { it.idList == idList }.sortedBy { it.idx }
        return if (filtered.isEmpty()) 1 else filtered.last().idx + 1
    }

    override fun getCardCount(idBoard: Int, idList: Int): Int {
        return cards.count { it.idBoard == idBoard && it.idList == idList }
    }

    override fun updateCard(card: Card, archived: Boolean, description: String, endDate: String?) {
        card.archived = archived
        card.description = description
        card.endDate = endDate
    }

    private fun getNextId(): Int {
        return if (cards.isEmpty()) 1 else cards.last().idCard + 1
    }
}
