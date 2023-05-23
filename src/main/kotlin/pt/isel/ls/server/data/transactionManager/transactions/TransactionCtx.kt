package pt.isel.ls.server.data.transactionManager.transactions

interface TransactionCtx {
    fun init()
    fun commit()
    fun rollback()
}