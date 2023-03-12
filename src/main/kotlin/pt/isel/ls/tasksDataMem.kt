package pt.isel.ls
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class User(val idUser : Int = 0 , val email : String, val name : String, val token : String = "")
@Serializable
data class Board(val idUser : Int = 0, val idBoard : Int = 0 , val description : String, val name : String)
@Serializable
data class BoardList(val idBoard : Int = 0, val idList : Int = 0, val phase : String, val name : String)
@Serializable
data class Card(val idList : Int = 0, val idCard: Int = 0, val name: String, val description: String, val startDate: Date, val endDate: Date, val archived: Boolean)

val users = mutableListOf<User>()
val nextId = 0
val boards = mutableListOf<Board>()
val boardList = mutableListOf<BoardList>()
val cards = mutableListOf<Card>()

fun addUser(user : User){
    users.add(user)
}
