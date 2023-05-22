package pt.isel.ls.server.data.dataMem

import pt.isel.ls.server.data.dataInterfaces.ConnectionDummy
import pt.isel.ls.server.data.dataInterfaces.DataExecutor
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.exceptions.map
import java.sql.Connection
import java.sql.SQLException

class DataExecutorMem : DataExecutor {
    override fun <R> execute(action: (Connection) -> R): R {
        val con = ConnectionDummy()
        try {
            return action(con)
        } catch (e: Exception) {
            println(e)
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
