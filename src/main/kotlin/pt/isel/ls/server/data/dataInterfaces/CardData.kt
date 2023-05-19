package pt.isel.ls.server.data.dataInterfaces

import pt.isel.ls.server.utils.Card
import java.time.LocalDateTime
import java.util.*

interface CardData {
    val size: Int

    fun createCard(
        idList: Int,
        idBoard: Int,
        name: String,
        description: String? = null,
        endDate: String? = null
    ): Int // check endDate

    fun getCardsFromList(idList: Int, idBoard: Int, limit: Int?, skip: Int?): List<Card>

    fun getCard(idCard: Int, idBoard: Int): Card

    fun moveCard(idCard: Int, idBoard: Int, idListDst: Int, idxDst: Int)

    fun deleteCard(idCard: Int, idBoard: Int)

    fun deleteCards(idList: Int)

    fun archiveCards(idList: Int)

    fun getNextIdx(idList: Int): Int

    fun getCardCount(idBoard: Int, idList: Int): Int

    fun updateCard(card: Card, archived: Boolean, description: String, endDate: String?)
}
