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
        var userId: Int

        dataSource.connection.use {
            it.autoCommit = false
            /*val ps = it.prepareStatement(insertStmt, Statement.RETURN_GENERATED_KEYS)
            ps.executeUpdate()

            if (ps.generatedKeys.next()) userId = ps.generatedKeys.getInt(1)*/
            val res = it.prepareStatement(insertStmt).executeQuery()
            res.next()

            userId = res.getInt("idUser")

            it.autoCommit = true
        }
        println("returned id: $userId")
        return Pair(userId, token)
    }

    override fun getUser(token: String): User {
        val dataSource = setup()
        val selectStmt = UserStatements.getUserCMD(token)
        var idUser: Int
        lateinit var email: String
        lateinit var name: String
        var avatar: String

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
        lateinit var avatar: String


        dataSource.connection.use {
            it.autoCommit = false
            val res = it.prepareStatement(selectStmt).executeQuery()
            res.next()

            if (res.row == 0) throw TrelloException.NotFound("User")

            email = res.getString("email")
            name = res.getString("name")
            token = res.getString("token")
            avatar = res.getString("avatar")

            it.autoCommit = true
        }
        return User(idUser, email, name, token, avatar)
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

    override fun getUsers(idBoard: Int, limit: Int?, skip: Int?): List<User> {
        val dataSource = setup()
        val selectStmt = UserStatements.getUsersFromBoard(idBoard, limit, skip)
        val userList = mutableListOf<User>()

        dataSource.connection.use {
            it.autoCommit = false
            val res = it.prepareStatement(selectStmt).executeQuery()

            while (res.next()) {
                val user = User(
                    res.getInt("idUser"),
                    res.getString("email"),
                    res.getString("name"),
                    res.getString("token"),
                    res.getString("avatar")
                )
                userList.add(user)
            }

            it.autoCommit = true
        }
        return userList
    }

    override fun changeAvatar(token: String, avatar: String) {
        val dataSource = setup()
        val updateStmt = UserStatements.changeAvatarCMD(token, avatar)

        dataSource.connection.use {
            it.autoCommit = false

            it.prepareStatement(updateStmt).executeUpdate()

            it.autoCommit = true
        }
    }
}
