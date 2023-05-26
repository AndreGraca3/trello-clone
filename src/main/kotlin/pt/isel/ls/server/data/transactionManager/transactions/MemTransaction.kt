package pt.isel.ls.server.data.transactionManager.transactions

import java.sql.Connection

class MemTransaction() : TransactionCtx {
    override val con: Connection get() = throw UnsupportedOperationException("Memory transaction does not have a connection.")

    override fun init() {
        // Do nothing...
    }

    override fun commit() {
        // Do nothing...
    }

    override fun rollback() {
        // Do nothing...
    }
}