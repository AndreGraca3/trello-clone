package pt.isel.ls.server.data.dataPostGres.dataSQL.models

import pt.isel.ls.server.data.dataInterfaces.models.UserData
import pt.isel.ls.server.data.dataPostGres.statements.UserStatements
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.User
import pt.isel.ls.server.utils.setup
import java.sql.Connection
import java.util.*

class UserDataSQL : UserData {

    override fun createUser(name: String, email: String, con: Connection): Pair<Int, String> {
        val token = UUID.randomUUID().toString()
        val insertStmt = UserStatements.createUserCMD(email, name, token)
        val userId: Int

        val res = con.prepareStatement(insertStmt).executeQuery()
        res.next()

        userId = res.getInt("idUser")

        return Pair(userId, token)
    }

    override fun getUser(token: String, con: Connection): User {
        val selectStmt = UserStatements.getUserCMD(token)
        val idUser: Int
        lateinit var email: String
        lateinit var name: String
        val avatar: String

        val res = con.prepareStatement(selectStmt).executeQuery()
        res.next()

        if (res.row == 0) throw TrelloException.NotFound("User")

        idUser = res.getInt("idUser")
        email = res.getString("email")
        name = res.getString("name")
        avatar = res.getString("avatar")

        return User(idUser, email, name, token, avatar)
    }

    override fun getUser(idUser: Int, con: Connection): User {
        val selectStmt = UserStatements.getUserCMD(idUser)
        lateinit var email: String
        lateinit var name: String
        lateinit var token: String
        lateinit var avatar: String

        val res = con.prepareStatement(selectStmt).executeQuery()
        res.next()

        if (res.row == 0) throw TrelloException.NotFound("User")

        email = res.getString("email")
        name = res.getString("name")
        token = res.getString("token")
        avatar = res.getString("avatar")

        return User(idUser, email, name, token, avatar)
    }

    override fun getUsers(idBoard: Int, limit: Int?, skip: Int?, con: Connection): List<User> {
        val selectStmt = UserStatements.getUsersFromBoard(idBoard, limit, skip)
        val userList = mutableListOf<User>()

        val res = con.prepareStatement(selectStmt).executeQuery()

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

        return userList
    }

    override fun changeAvatar(token: String, avatar: String, con: Connection) {
        val updateStmt = UserStatements.changeAvatarCMD(token, avatar)
        con.prepareStatement(updateStmt).executeUpdate()
    }
}
