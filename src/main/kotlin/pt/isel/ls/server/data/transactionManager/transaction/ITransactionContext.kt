package pt.isel.ls.server.data.transactionManager.transaction

interface ITransactionContext {
    fun init()
    fun commit()
    fun rollback()
}