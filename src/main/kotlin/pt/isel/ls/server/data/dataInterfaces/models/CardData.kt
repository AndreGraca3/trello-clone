package pt.isel.ls.server.data.dataInterfaces.models

import pt.isel.ls.server.data.transactionManager.transaction.ITransactionContext
import pt.isel.ls.server.utils.Card

interface CardData {

    fun createCard(
        idList: Int,
        idBoard: Int,
        name: String,
        description: String? = null,
        endDate: String? = null,
        ctx: ITransactionContext
    ): Int

    fun getCardsFromList(idList: Int, idBoard: Int, ctx: ITransactionContext): List<Card>

    fun getCard(idCard: Int, idBoard: Int, ctx: ITransactionContext): Card

    fun moveCard(
        idCard: Int,
        idBoard: Int,
        idList: Int,
        idListDst: Int,
        idx: Int,
        idxDst: Int,
        ctx: ITransactionContext
    )

    fun decreaseIdx(idList: Int, idx: Int, ctx: ITransactionContext)

    fun deleteCard(idCard: Int, idBoard: Int, ctx: ITransactionContext)

    fun deleteCards(idList: Int, ctx: ITransactionContext)

    fun archiveCards(idBoard: Int, idList: Int, ctx: ITransactionContext)

    fun getNextIdx(idList: Int, ctx: ITransactionContext): Int

    fun getCardCount(idBoard: Int, idList: Int, ctx: ITransactionContext): Int // can be deleted, it's not being used.

    fun getArchivedCards(idBoard: Int, ctx: ITransactionContext): List<Card>

    fun updateCard(card: Card, description: String?, endDate: String?, idList: Int?, archived: Boolean, ctx: ITransactionContext)
}
