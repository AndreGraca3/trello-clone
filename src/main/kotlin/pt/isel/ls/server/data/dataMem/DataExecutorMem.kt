package pt.isel.ls.server.data.dataMem

import pt.isel.ls.server.data.dataInterfaces.ConnectionDB
import pt.isel.ls.server.data.dataInterfaces.DataExecutor
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.exceptions.map
import java.sql.Connection
import java.sql.SQLException

class DataExecutorMem<R> : DataExecutor<R> {
    override fun execute(action: (Connection) -> R): R {
        val con = ConnectionDB()
        try {
            return action(con)
        } catch (e: Exception) {
            println(e)
            if(e is SQLException) {
                val trelloException = map[e.sqlState] ?: throw TrelloException.InternalError()
                throw trelloException(e.localizedMessage)
            }
            else throw e as TrelloException
        }
    }
}