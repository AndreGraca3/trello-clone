package pt.isel.ls.server.data.dataPostGres.statements

object UserStatements {

    fun createUserCMD(email: String, name: String, token: String): String {
        return "INSERT INTO dbo.user (email, name, token) VALUES ('$email', '$name', '$token');"
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
}
