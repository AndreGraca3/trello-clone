package pt.isel.ls.server.data.dataPostGres.dataSQL

import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.server.data.dataInterfaces.UserData
import pt.isel.ls.server.data.dataPostGres.statements.UserStatements
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.User
import java.util.*

class UserDataSQL: UserData {

    private fun setup(): PGSimpleDataSource {
        val dataSource = PGSimpleDataSource()
        val jdbcDatabaseURL = System.getenv("JDBC_DATABASE_URL")
        dataSource.setURL(jdbcDatabaseURL)
        return dataSource
    }

    override fun createUser(name: String, email: String): Pair<Int, String> {
        val dataSource = setup()
        val token = UUID.randomUUID().toString()
        val insertStmt = UserStatements.createUserCMD(email, name, token)
        val selectStmt = UserStatements.getUserCMD(token)
        var userId = -1
        dataSource.connection.use {
            it.autoCommit = false
            val stmt1 = it.prepareStatement(insertStmt)
            stmt1.executeUpdate()

            val stmt2 = it.prepareStatement(selectStmt)
            val res = stmt2.executeQuery()

            while (res.next()) {
                userId = res.getInt("idUser")
            }
            it.autoCommit = true
        }
        return Pair(userId, token)
    }

    override fun getUser(token: String): User {
        TODO("Not yet implemented")
    }

    override fun checkEmail(email: String) {
        val dataSource = setup()
        val selectStmt = UserStatements.getUserByEmailCMD(email)
        dataSource.connection.use {
            it.autoCommit = false
            val res = it.prepareStatement(selectStmt).executeQuery()

            while (res.next()) {
                if(res.getString("email") != null) throw TrelloException.AlreadyExists(email)
            }
            it.autoCommit = true
        }
    }

}