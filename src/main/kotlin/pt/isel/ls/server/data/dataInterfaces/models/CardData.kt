package pt.isel.ls.server.data.dataInterfaces.models

import pt.isel.ls.server.utils.Card
import java.sql.Connection
import java.time.LocalDateTime
import java.util.*

interface CardData {

    fun createCard(
        idList: Int,
        idBoard: Int,
        name: String,
        description: String? = null,
        endDate: String? = null,
        con: Connection
    ): Int

    fun getCardsFromList(idList: Int, idBoard: Int, con: Connection): List<Card>

    fun getCard(idCard: Int, idBoard: Int, con: Connection): Card

    fun moveCard(
        idCard: Int,
        idBoard: Int,
        idList: Int,
        idListDst: Int,
        idx: Int,
        idxDst: Int,
        con: Connection
    )

    fun deleteCard(idCard: Int, idBoard: Int, con: Connection)

    fun deleteCards(idList: Int, con: Connection)

    fun archiveCards(idList: Int, con: Connection)

    fun getNextIdx(idList: Int, con: Connection): Int

    fun getCardCount(idBoard: Int, idList: Int, con: Connection): Int

    fun updateCard(card: Card, archived: Boolean, description: String, endDate: String?, con: Connection)
}
