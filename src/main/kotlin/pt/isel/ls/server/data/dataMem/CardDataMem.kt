package pt.isel.ls.server.data.dataMem

import pt.isel.ls.server.data.dataInterfaces.CardData
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.Card
import pt.isel.ls.server.utils.checkPaging
import java.time.LocalDate

class CardDataMem : CardData {

    val cards = mutableListOf<Card>()

    override val size get() = cards.size

    override fun createCard(idList: Int, idBoard: Int, name: String, description: String, endDate: String?): Int {
        val newCard =
            Card(
                getNextId(),
                idList,
                idBoard,
                name,
                description,
                LocalDate.now().toString(),
                endDate,
                false,
                getNextIdx(idList)
            )
        cards.add(newCard)
        return newCard.idCard
    }

    override fun getCardsFromList(idList: Int, idBoard: Int, limit: Int?, skip: Int?): List<Card> {
        val res = cards.filter { it.idList == idList && it.idBoard == idBoard }
        return checkPaging(res, limit, skip)
    }

    override fun getCard(idCard: Int, idList: Int, idBoard: Int): Card {
        return cards.find { it.idCard == idCard && it.idList == idList && it.idBoard == idBoard }
            ?: throw TrelloException.NotFound("Card")
    }

    override fun moveCard(idCard: Int, idListNow: Int, idBoard: Int, idListDst: Int, idxDst: Int) {
        val card = getCard(idCard, idListNow, idBoard)
        cards.forEach {
            /** This has a bug if we try to move to the same list but != index. **/
            if (it.idList == idListNow && it.idx >= idxDst) it.idx--
            if (it.idList == idListDst && it.idx >= idxDst) it.idx++
        }
        card.idList = idListDst
        card.idx = idxDst
    }

    override fun deleteCard(idCard: Int, idList: Int, idBoard: Int) {
        val card = getCard(idCard, idList, idBoard)
        if(!cards.remove(card)) throw TrelloException.NoContent("card")
        cards.forEach {
            if (it.idList == idList && it.idx >= card.idx) it.idx--
        }
    }

    override fun getNextIdx(idList: Int): Int {
        val filtered = cards.filter { it.idList == idList }
        return if (filtered.isEmpty()) 0 else filtered.last().idx + 1
    }

    override fun getCardCount(idBoard: Int, idList: Int): Int {
        return cards.count { it.idBoard == idBoard && it.idList == idList }
    }

    private fun getNextId(): Int {
        return if (cards.isEmpty()) 0 else cards.last().idCard + 1
    }
}
