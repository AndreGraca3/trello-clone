package pt.isel.ls

import java.util.UUID.randomUUID

fun createUser(name : String, email : String) : User {
    val token = randomUUID().toString()
    val user = User(nextId,email,name,token)
    addUser(user)
    println(users)
    return user
}