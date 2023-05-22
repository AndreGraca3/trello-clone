package pt.isel.ls.server.data.transactionManager.factory

import pt.isel.ls.server.data.transactionManager.transaction.ITransactionContext

interface ITransactionContextFactory {
    fun createTransaction(): ITransactionContext
}