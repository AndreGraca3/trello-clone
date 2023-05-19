package pt.isel.ls.server.data.dataPostGres.statements

object UserStatements {

    fun size(): String {
        return "SELECT COUNT(idUser) FROM dbo.user;"
    }

    fun createUserCMD(email: String, name: String, token: String): String {
        return "INSERT INTO dbo.user (email, name, token, avatar) VALUES ('$email', '$name', '$token', 'https://i.imgur.com/JGtwTBw.png') returning idUser;"
    }

    fun getUserCMD(token: String): String {
        return "SELECT * FROM dbo.user WHERE token = '$token';"
    }

    fun getUserCMD(idUser: Int): String {
        return "SELECT * FROM dbo.user WHERE idUser = $idUser;"
    }

    fun getUserByEmailCMD(email: String): String {
        return "SELECT * FROM dbo.user WHERE email = '$email';"
    }

    fun getUsersFromBoard(idBoard: Int, limit: Int?, skip: Int?): String {
        //val idUsersString = idUsers.toString().replace("[", "(").replace("]", ")")
        //return "SELECT * from dbo.user where idUser IN $idUsersString LIMIT $limit OFFSET $skip;"
        return "SELECT u.iduser, u2.name, u2.email, u2.token, u2.avatar FROM dbo.user_board u\n" +
                "inner join dbo.user u2 on u2.iduser = u.iduser\n" +
                "where u.idboard = $idBoard\n"+
                "LIMIT $limit OFFSET $skip;"
    }

    fun changeAvatarCMD(token: String, avatar: String): String {
        return "UPDATE dbo.user SET avatar = '$avatar' WHERE token = '$token';"
    }
}
