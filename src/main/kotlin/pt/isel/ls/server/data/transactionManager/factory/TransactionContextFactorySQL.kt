package pt.isel.ls.server.data.transactionManager.factory

import pt.isel.ls.server.data.transactionManager.transaction.ITransactionContext
import pt.isel.ls.server.data.transactionManager.transaction.SQLTransaction

class TransactionContextFactorySQL: ITransactionContextFactory {
    override fun createTransaction(): ITransactionContext {
        return SQLTransaction()
    }
}