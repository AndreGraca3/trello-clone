package pt.isel.ls.server.data.transactionManager.transactions

import java.sql.Connection

interface TransactionCtx {
    val con: Connection
    fun init()
    fun commit()
    fun rollback()
}
