package pt.isel.ls
import kotlinx.serialization.Serializable
@Serializable
data class User(val idUser : Int = 0 , val email : String, val name : String, val token : String = "")

val users = mutableListOf<User>()
val nextId = 0

fun addUser(user : User){
    users.add(user)
}
