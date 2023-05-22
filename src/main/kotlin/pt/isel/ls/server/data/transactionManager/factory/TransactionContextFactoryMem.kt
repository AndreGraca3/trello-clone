package pt.isel.ls.server.data.transactionManager.factory

import pt.isel.ls.server.data.transactionManager.transaction.ITransactionContext
import pt.isel.ls.server.data.transactionManager.transaction.MemTransaction

class TransactionContextFactoryMem: ITransactionContextFactory {
    override fun createTransaction(): ITransactionContext {
        return MemTransaction()
    }
}