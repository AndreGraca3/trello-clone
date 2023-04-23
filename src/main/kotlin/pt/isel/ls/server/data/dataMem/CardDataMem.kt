package pt.isel.ls.server.data.dataMem

import pt.isel.ls.server.data.dataInterfaces.CardData
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.Card
import pt.isel.ls.server.utils.checkPaging
import java.time.LocalDate

class CardDataMem : CardData {

    val cards = mutableListOf<Card>(
        Card(0, 0, 0, "Card0", "this is description0", "2020-01-01", "2020-01-02", false, 0),
        Card(1, 0, 0, "Card1", "this is description1", "2020-01-01", "2020-01-02", false, 1),
        Card(2, 0, 0, "Card2", "this is description2", "2020-01-01", "2020-01-02", false, 2),
        Card(15, 0, 0, "Card15", "this is description15", "2020-01-01", "2020-01-02", false, 3),
        Card(16, 0, 0, "Card16", "this is description16", "2020-01-01", "2020-01-02", false, 4),
        Card(17, 0, 0, "Card17", "this is description17", "2020-01-01", "2020-01-02", false, 5),
        Card(3, 1, 0, "Card3", "this is description3", "2020-01-01", "2020-01-02", false, 0),
        Card(4, 1, 0, "Card4", "this is description4", "2020-01-01", "2020-01-02", false, 1),
        Card(5, 1, 0, "Card5", "this is description5", "2020-01-01", "2020-01-02", false, 2),
        Card(6, 2, 0, "Card6", "this is description6", "2020-01-01", "2020-01-02", false, 0),
        Card(7, 2, 0, "Card7", "this is description7", "2020-01-01", "2020-01-02", false, 1),
        Card(8, 2, 0, "Card8", "this is description8", "2020-01-01", "2020-01-02", false, 2),
        Card(9, 3, 0, "Card9", "this is description9", "2020-01-01", "2020-01-02", false, 0),
        Card(10, 3, 0, "Card10", "this is description10", "2020-01-01", "2020-01-02", false, 1),
        Card(11, 3, 0, "Card11", "this is description11", "2020-01-01", "2020-01-02", false, 2),
        Card(12, 4, 0, "Card12", "this is description12", "2020-01-01", "2020-01-02", false, 0),
        Card(13, 4, 0, "Card13", "this is description13", "2020-01-01", "2020-01-02", false, 1),
        Card(14, 4, 0, "Card14", "this is description14", "2020-01-01", "2020-01-02", false, 2),
    )

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

    override fun getCardsFromList(idList: Int, idBoard: Int, limit: Int, skip: Int): List<Card> {
        return cards.filter { it.idList == idList && it.idBoard == idBoard }.subList(skip, limit).sortedBy { it.idx }
    }

    override fun getCard(idCard: Int, idList: Int, idBoard: Int): Card {
        return cards.find { it.idCard == idCard && it.idList == idList && it.idBoard == idBoard }
            ?: throw TrelloException.NotFound("Card")
    }

    override fun moveCard(idCard: Int, idListNow: Int, idBoard: Int, idListDst: Int, idxDst: Int) {
        val card = getCard(idCard, idListNow, idBoard)
        cards.forEach {
            if (it.idList == idListNow && it.idx >= card.idx) it.idx--
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
        val filtered = cards.filter { it.idList == idList }.sortedBy { it.idx }
        return if (filtered.isEmpty()) 0 else filtered.last().idx + 1
    }

    override fun getCardCount(idBoard: Int, idList: Int): Int {
        return cards.count { it.idBoard == idBoard && it.idList == idList }
    }

    private fun getNextId(): Int {
        return if (cards.isEmpty()) 0 else cards.last().idCard + 1
    }
}
