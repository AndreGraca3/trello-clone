package pt.isel.ls.server.data.dataInterfaces.models

import pt.isel.ls.server.data.transactionManager.transactions.TransactionCtx
import pt.isel.ls.server.utils.Card

interface CardData {

    fun createCard(
        idList: Int,
        idBoard: Int,
        name: String,
        description: String? = null,
        endDate: String? = null,
        ctx: TransactionCtx
    ): Int

    fun getCardsFromList(idList: Int, idBoard: Int, ctx: TransactionCtx): List<Card>

    fun getCard(idCard: Int, idBoard: Int, ctx: TransactionCtx): Card

    fun moveCard(
        idCard: Int,
        idBoard: Int,
        idList: Int,
        idListDst: Int,
        idx: Int,
        idxDst: Int,
        ctx: TransactionCtx
    )

    fun decreaseIdx(idList: Int, idx: Int, ctx: TransactionCtx)

    fun deleteCard(idCard: Int, idBoard: Int, ctx: TransactionCtx)

    fun deleteCards(idList: Int, ctx: TransactionCtx)

    fun archiveCards(idBoard: Int, idList: Int, ctx: TransactionCtx)

    fun getNextIdx(idList: Int, ctx: TransactionCtx): Int

    fun getCardCount(idBoard: Int, idList: Int, ctx: TransactionCtx): Int // can be deleted, it's not being used.

    fun getArchivedCards(idBoard: Int, ctx: TransactionCtx): List<Card>

    fun updateCard(card: Card, description: String?, endDate: String?, idList: Int?, archived: Boolean, ctx: TransactionCtx)
}
