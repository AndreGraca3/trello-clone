package pt.isel.ls.server.data.dataInterfaces

import java.sql.Connection

interface DataExecutor {
    fun <R> execute(action: (Connection) -> R): R
}
