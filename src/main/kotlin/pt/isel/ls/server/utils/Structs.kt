package pt.isel.ls.server.utils

import kotlinx.serialization.Serializable

// Model Classes
@Serializable
data class User(val idUser: Int, val email: String, val name: String, val token: String, var avatar: String)

data class UserBoard(val idUser: Int, val idBoard: Int)

@Serializable
data class Board(val idBoard: Int, val name: String, val description: String)

@Serializable
data class BoardList(val idList: Int, val idBoard: Int, val name: String)

@Serializable
data class Card(
    val idCard: Int,
    var idList: Int?,
    val idBoard: Int,
    val name: String,
    var description: String?,
    val startDate: String,
    var endDate: String?,
    var archived: Boolean,
    var idx: Int
)

// Aux Structs
@Serializable
data class IDUser(val idUser: Int)

@Serializable
data class Avatar(val imgUrl: String)

@Serializable
data class NewList(val idListNow: Int, val idListDst: Int, val cix: Int)

@Serializable
data class UserIn(val name: String, val email: String)

@Serializable
data class UserOut(val idUser: Int, val token: String)

@Serializable
data class BoardIn(val name: String, val description: String)

@Serializable
data class BoardOut(val idBoard: Int)

@Serializable
data class BoardWithLists(val idBoard: Int, val name: String, val description: String, val numLists: Int)

@Serializable
data class BoardListIn(val name: String)

@Serializable
data class DeleteListIn(val boardId: Int, val listId: Int)

@Serializable
data class CardIn(
    val name: String,
    val description: String?,
    val endDate: String?,
    val idList: Int
)

@Serializable
data class Changes(
    val archived: Boolean,
    val description: String,
    val endDate: String,
    val idList: Int?
)

@Serializable
data class TotalBoards(
    val totalBoards: Int,
    val boards: List<BoardWithLists>
)

// DETAILED OBJECTS
@Serializable
data class BoardDetailed(
    val idBoard: Int,
    val name: String,
    val description: String,
    val lists: List<ListDetailed>,
    val archivedCards: List<Card>
)

@Serializable
data class ListDetailed(
    val idList: Int,
    val idBoard: Int,
    val name: String,
    val cards: List<Card>
)