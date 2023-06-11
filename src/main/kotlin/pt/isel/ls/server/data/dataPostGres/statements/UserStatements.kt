package pt.isel.ls.server.data.dataPostGres.statements

object UserStatements {

    fun size(): String {
        return "SELECT COUNT(idUser) FROM dbo.user;"
    }

    fun createUserCMD(email: String, name: String, token: String, password: String, avatar: String?): String {
        return "INSERT INTO dbo.user (email, name, token, password, avatar) VALUES ('$email', '$name', '$token', '$password', '$avatar') returning idUser;"
    }

    fun getUserCMD(token: String): String {
        return "SELECT * FROM dbo.user WHERE token = '$token';"
    }

    fun getUserCMD(idUser: Int): String {
        return "SELECT * FROM dbo.user WHERE idUser = $idUser;"
    }

    fun loginCMD(email: String, hashedPassword: String): String {
        return "SELECT token FROM dbo.user u WHERE u.email = '$email' AND u.password = '$hashedPassword';"
    }

    fun getUsersFromBoard(idBoard: Int): String {
        return "SELECT u.iduser, u2.name, u2.email, u2.token, u2.password, u2.avatar FROM dbo.user_board u\n" +
            "inner join dbo.user u2 on u2.iduser = u.iduser\n" +
            "where u.idboard = $idBoard;"
    }

    fun changeAvatarCMD(token: String, avatar: String): String {
        return "UPDATE dbo.user SET avatar = '$avatar' WHERE token = '$token';"
    }

    fun getUserEmailCMD(email: String): String {
        return "SELECT * FROM dbo.user WHERE email = '$email';"
    }

    fun getUserProfile(idUser: Int): String {
        return "SELECT name, avatar FROM dbo.user WHERE idUser = $idUser;"
    }
}
