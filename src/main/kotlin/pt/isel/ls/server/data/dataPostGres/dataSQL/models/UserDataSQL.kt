package pt.isel.ls.server.data.dataPostGres.dataSQL.models

import pt.isel.ls.server.User
import pt.isel.ls.server.data.dataInterfaces.models.UserData
import pt.isel.ls.server.data.dataPostGres.statements.UserStatements
import pt.isel.ls.server.data.transactionManager.transactions.TransactionCtx
import pt.isel.ls.server.exceptions.TrelloException
import java.util.*

class UserDataSQL : UserData {

    override fun createUser(
        name: String,
        email: String,
        hashedPassword: String,
        urlAvatar: String?,
        ctx: TransactionCtx
    ): Pair<Int, String> {
        val token = UUID.randomUUID().toString()
        val insertStmt = UserStatements.createUserCMD(email, name, token, hashedPassword, urlAvatar)

        val res = ctx.con.prepareStatement(insertStmt).executeQuery()
        res.next()

        return Pair(res.getInt("idUser"), token)
    }

    override fun getUser(token: String, ctx: TransactionCtx): User {
        val selectStmt = UserStatements.getUserCMD(token)

        val res = ctx.con.prepareStatement(selectStmt).executeQuery()
        res.next()

        if (res.row == 0) throw TrelloException.NotAuthorized() // NotFound("$NOT_FOUND User")

        return User(
            res.getInt("idUser"),
            res.getString("email"),
            res.getString("name"),
            token,
            res.getString("password"),
            res.getString("avatar")
        )
    }

    override fun getUserByEmail(email: String, ctx: TransactionCtx): User {
        val selectStmt = UserStatements.getUserEmailCMD(email)
        val idUser: Int
        lateinit var name: String
        lateinit var token: String
        lateinit var password: String
        lateinit var avatar: String

        val res = ctx.con.prepareStatement(selectStmt).executeQuery()
        res.next()

        if (res.row == 0) throw TrelloException.NotFound("User")

        idUser = res.getInt("idUser")
        name = res.getString("name")
        token = res.getString("token")
        password = res.getString("password")
        avatar = res.getString("avatar")

        return User(idUser, email, name, token, password, avatar)
    }

    override fun getUser(idUser: Int, ctx: TransactionCtx): User {
        val selectStmt = UserStatements.getUserCMD(idUser)

        val res = ctx.con.prepareStatement(selectStmt).executeQuery()
        res.next()

        if (res.row == 0) throw TrelloException.NotFound("User")

        return User(
            idUser,
            res.getString("email"),
            res.getString("name"),
            res.getString("token"),
            res.getString("password"),
            res.getString("avatar")
        )
    }

    override fun getUsers(idBoard: Int, ctx: TransactionCtx): List<User> {
        val selectStmt = UserStatements.getUsersFromBoard(idBoard)
        val userList = mutableListOf<User>()

        val res = ctx.con.prepareStatement(selectStmt).executeQuery()

        while (res.next()) {
            val user = User(
                res.getInt("idUser"),
                res.getString("email"),
                res.getString("name"),
                res.getString("token"),
                res.getString("password"),
                res.getString("avatar")
            )
            userList.add(user)
        }

        return userList
    }

    override fun changeAvatar(token: String, avatar: String, ctx: TransactionCtx) {
        val updateStmt = UserStatements.changeAvatarCMD(token, avatar)
        ctx.con.prepareStatement(updateStmt).executeUpdate()
    }

    override fun login(email: String, hashedPassword: String, ctx: TransactionCtx): String {
        val selectStmt = UserStatements.loginCMD(email, hashedPassword)

        val res = ctx.con.prepareStatement(selectStmt).executeQuery()
        res.next()

        if (res.row == 0) throw TrelloException.NotAuthorized()

        return res.getString("token")
    }
}
