package pt.isel.ls.server.data.dataInterfaces

import java.sql.Connection

interface DataExecutor<R> {
    fun execute(action: (Connection) -> R): R
}
