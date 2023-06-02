package pt.isel.ls.server.data.transactionManager.transactions

import org.postgresql.ds.PGSimpleDataSource
import java.sql.Connection

class SQLTransaction: TransactionCtx {
    override val con: Connection = setup().connection

    override fun init() {
        con.autoCommit = false
    }

    override fun commit() {
        con.commit()
    }

    override fun rollback() {
        con.rollback()
    }

    private fun setup(): PGSimpleDataSource {
        val dataSource = PGSimpleDataSource()
        val jdbcDatabaseURL = System.getenv("JDBC_DATABASE_URL")
        dataSource.setURL(jdbcDatabaseURL)
        return dataSource
    }
}