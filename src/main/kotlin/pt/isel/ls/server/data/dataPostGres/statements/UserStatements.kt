package pt.isel.ls.server.data.dataPostGres.statements

object UserStatements {

    fun size(): String {
        return "SELECT COUNT(idUser) FROM dbo.user;"
    }

    fun createUserCMD(email: String, name: String, token: String): String {
        return "INSERT INTO dbo.user (email, name, token) VALUES ('$email', '$name', '$token', 'https://i.imgur.com/JGtwTBw.png');"
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

    fun getUsersByIds(idUsers: List<Int>, limit: Int, skip: Int): String {
        val idUsersString = idUsers.toString().replace("[", "(").replace("]", ")")
        return "SELECT * from dbo.user where idUser IN $idUsersString LIMIT $limit OFFSET $skip;"
    }

    fun changeAvatarCMD(idUser: Int, avatar: String): String {
        return "UPDATE dbo.user SET avatar = '$avatar' WHERE idUser = $idUser"
    }
}
