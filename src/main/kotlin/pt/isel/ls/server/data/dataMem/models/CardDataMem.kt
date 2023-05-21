package pt.isel.ls.server.data.dataMem.models

import pt.isel.ls.server.data.dataInterfaces.models.CardData
import pt.isel.ls.server.data.dataMem.cards
import pt.isel.ls.server.exceptions.NOT_FOUND
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.Card
import java.sql.Connection
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CardDataMem : CardData {


    override fun createCard(
        idList: Int,
        idBoard: Int,
        name: String,
        description: String?,
        endDate: String?,
        con: Connection
    ): Int {
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
                getNextIdx(idList, con)
            )
        cards.add(newCard)
        return newCard.idCard
    }

    override fun getCardsFromList(idList: Int, idBoard: Int, con: Connection): List<Card> {
        return cards.filter { it.idList == idList && it.idBoard == idBoard }.sortedBy { it.idx }
    }

    override fun getCard(idCard: Int, idBoard: Int, con: Connection): Card {
        return cards.find { it.idCard == idCard && it.idBoard == idBoard }
            ?: throw TrelloException.NotFound("Card $NOT_FOUND")
    }

    override fun moveCard(
        idCard: Int,
        idBoard: Int,
        idList: Int,
        idListDst: Int,
        idx: Int,
        idxDst: Int,
        con: Connection
    ) {
        val card = getCard(idCard, idBoard, con)
        val cardIdx = card.idx
        cards.forEach {
            if (it.idList == card.idList && it.idx >= cardIdx) it.idx--
            if (it.idList == idListDst && it.idx >= idxDst) it.idx++
        }
        card.idList = idListDst
        card.idx = idxDst
    }

    override fun deleteCard(idCard: Int, idBoard: Int, con: Connection) {
        val card: Card
        try {
            card = getCard(idCard, idBoard, con)
        } catch (ex: TrelloException) {
            throw TrelloException.NoContent()
        }
        cards.remove(card)
        if (card.idList == null) return
        cards.forEach {
            if (it.idList == card.idList && it.idx >= card.idx) it.idx--
        }
    }

    override fun deleteCards(idList: Int, con: Connection) {
        val filtered = cards.filter { it.idList == idList }
        cards.removeAll(filtered)
    }

    override fun archiveCards(idList: Int, con: Connection) {
        val filtered = cards.filter { it.idList == idList }
        filtered.forEach {
            it.archived = true
            it.idList = null
        }
    }

    override fun getNextIdx(idList: Int, con: Connection): Int {
        val filtered = cards.filter { it.idList == idList }.sortedBy { it.idx }
        return if (filtered.isEmpty()) 1 else filtered.last().idx + 1
    }

    override fun getCardCount(idBoard: Int, idList: Int, con: Connection): Int {
        return cards.count { it.idBoard == idBoard && it.idList == idList }
    }

    override fun updateCard(card: Card, archived: Boolean, description: String, endDate: String?, con: Connection) {
        card.archived = archived
        card.description = description
        card.endDate = endDate
    }

    private fun getNextId(): Int {
        return if (cards.isEmpty()) 1 else cards.last().idCard + 1
    }
}
