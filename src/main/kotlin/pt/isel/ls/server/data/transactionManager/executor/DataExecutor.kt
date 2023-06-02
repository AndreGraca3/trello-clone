package pt.isel.ls.server.data.transactionManager.executor

import pt.isel.ls.server.data.transactionManager.transactions.TransactionCtx
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.exceptions.map
import java.sql.SQLException

class DataExecutor(private val tr: TransactionCtx) {
     fun <R> execute(action: (TransactionCtx) -> R): R {
         try {
            tr.init()
            val res = action(tr)
            tr.commit()
            return res
        } catch (e: Exception) {
            println(e)
            tr.rollback()
            if (e is SQLException) {
                val trelloException = map[e.sqlState] ?: throw TrelloException.InternalError()
                throw trelloException(e.localizedMessage.substringAfter("Detail: Key "))
            } else {
                throw e as TrelloException
            }
        }
    }
}
