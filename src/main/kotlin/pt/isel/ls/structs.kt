package pt.isel.ls

import kotlinx.serialization.Serializable

@Serializable
data class User(val idUser: Int, val email: String, val name: String, val token: String)
@Serializable
data class UserIn(val name: String, val email: String)
@Serializable
data class UserOut(val idUser : Int, val token : String)

@Serializable
data class Board(val idBoard: Int, val name: String, val description: String, val idUsers: MutableList<Int>)
@Serializable
data class BoardIn(val name: String, val description: String)
@Serializable
data class BoardOut(val idBoard: Int)

@Serializable
data class BoardList(val idList: Int, val idBoard: Int, val name: String)

@Serializable
data class BoardListIn(val name : String, val idBoard : Int) // não é necessário se os parametros vierem na path.

@Serializable
data class BoardListOut(val idList : Int)

@Serializable
data class Card(
    val idCard: Int,
    var idList: Int,
    val name: String,
    val description: String,
    val startDate: String,
    var endDate: String?,
    var archived: Boolean
)

@Serializable
data class CardIn(
    val idList: Int,
    val name: String,
    val startDate: String,
    val description: String,
    val endDate: String?
) // name and description should be vars

@Serializable
data class CardOut(val name : String) // TODO()