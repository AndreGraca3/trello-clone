package pt.isel.ls.server.data.transactionManager.executor

import pt.isel.ls.server.data.transactionManager.transaction.ITransactionContext
import pt.isel.ls.server.data.transactionManager.factory.ITransactionContextFactory
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.exceptions.map
import java.sql.SQLException

class DataExecutor(private val transactionFactory: ITransactionContextFactory) {
     fun <R> execute(action: (ITransactionContext) -> R): R {
         val tr = transactionFactory.createTransaction()
         try {
            tr.init()
            val res = action(tr)
            tr.commit()
            return res
        } catch (e: Exception) {
            println(e)
            tr.rollback()
            if (e is SQLException) {
                println(e.sqlState)
                val trelloException = map[e.sqlState] ?: throw TrelloException.InternalError()
                throw trelloException(e.localizedMessage)
            } else {
                throw e as TrelloException
            }
        }
    }
}
