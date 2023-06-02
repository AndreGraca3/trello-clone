package pt.isel.ls.server.security

import java.security.MessageDigest
import java.security.SecureRandom

fun hashPassword(password: String): String {

    val messageDigest = MessageDigest.getInstance("SHA-256")

    val hashedBytes = messageDigest.digest(password.toByteArray())
    val stringBuilder = StringBuilder()

    for (hashedByte in hashedBytes) {
        val hex = String.format("%02x", hashedByte)
        stringBuilder.append(hex)
    }

    return stringBuilder.toString()
}

fun generateSalt(): String { // Store the salt along with the hashed salted password for login verification
    val random = SecureRandom()
    val saltBytes = ByteArray(16)
    random.nextBytes(saltBytes)
    return saltBytes.joinToString("") { "%02x".format(it) }
}