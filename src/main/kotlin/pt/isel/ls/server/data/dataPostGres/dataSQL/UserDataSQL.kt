package pt.isel.ls.server.data.dataPostGres.dataSQL

import pt.isel.ls.server.data.dataInterfaces.UserData
import pt.isel.ls.server.data.dataPostGres.statements.UserStatements
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.User
import pt.isel.ls.server.utils.setup
import java.sql.Statement
import java.util.*

class UserDataSQL : UserData {

    override val size get() = getSizeCount("idUser", "user")

    override fun createUser(name: String, email: String): Pair<Int, String> {
        val dataSource = setup()
        val token = UUID.randomUUID().toString()
        val insertStmt = UserStatements.createUserCMD(email, name, token)
        var userId = -1

        dataSource.connection.use {
            it.autoCommit = false
            val ps = it.prepareStatement(insertStmt, Statement.RETURN_GENERATED_KEYS)
            ps.executeUpdate()

            if (ps.generatedKeys.next()) userId = ps.generatedKeys.getInt(1)

            it.autoCommit = true
        }
        return Pair(userId, token)
    }

    override fun getUser(token: String): User {
        val dataSource = setup()
        val selectStmt = UserStatements.getUserCMD(token)
        var idUser: Int
        lateinit var email: String
        lateinit var name: String
        var avatar: String?

        dataSource.connection.use {
            it.autoCommit = false
            val res = it.prepareStatement(selectStmt).executeQuery()
            res.next()

            if (res.row == 0) throw TrelloException.NotFound("User")

            idUser = res.getInt("idUser")
            email = res.getString("email")
            name = res.getString("name")
            avatar = res.getString("avatar")

            it.autoCommit = true
        }
        return User(idUser, email, name, token, avatar)
    }

    override fun getUser(idUser: Int): User {
        val dataSource = setup()
        val selectStmt = UserStatements.getUserCMD(idUser)
        lateinit var email: String
        lateinit var name: String
        lateinit var token: String

        dataSource.connection.use {
            it.autoCommit = false
            val res = it.prepareStatement(selectStmt).executeQuery()
            res.next()

            if (res.row == 0) throw TrelloException.NotFound("User")

            email = res.getString("email")
            name = res.getString("name")
            token = res.getString("token")

            it.autoCommit = true
        }
        return User(idUser, email, name, token)
    }

    override fun checkEmail(email: String) {
        val dataSource = setup()
        val selectStmt = UserStatements.getUserByEmailCMD(email)
        dataSource.connection.use {
            it.autoCommit = false
            val res = it.prepareStatement(selectStmt).executeQuery()
            res.next()

            if (res.row != 0) throw TrelloException.AlreadyExists(email)

            it.autoCommit = true
        }
    }

    override fun getUsers(idUsers: List<Int>, limit: Int, skip: Int): List<User> {
        val dataSource = setup()
        val selectStmt = UserStatements.getUsersByIds(idUsers, limit, skip)
        val userList = mutableListOf<User>()

        dataSource.connection.use {
            it.autoCommit = false
            val res = it.prepareStatement(selectStmt).executeQuery()

            while (res.next()) {
                val user = User(
                    res.getInt("idUSer"),
                    res.getString("email"),
                    res.getString("name"),
                    res.getString("token")
                )
                userList.add(user)
            }

            it.autoCommit = true
        }
        return userList
    }

    override fun changeAvatar(idUser: Int, avatar: String) {
        val dataSource = setup()
        val updateStmt = UserStatements.changeAvatarCMD(idUser, avatar)

        dataSource.connection.use {
            it.autoCommit = false
            it.prepareStatement(updateStmt).executeUpdate()
            it.autoCommit = true
        }
    }
}
