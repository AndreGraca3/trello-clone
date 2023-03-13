package pt.isel.ls

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class User(val idUser: Int=0, val email: String, val name: String, val token: String = "")

@Serializable
data class Board(val idUser: Int, val idBoard: Int, val description: String, val name: String)

@Serializable
data class BoardList(val idBoard: Int, val idList: Int, val phase: String, val name: String)

@Serializable
data class Card(
    val idList: Int,
    val idCard: Int,
    val name: String,
    val description: String,
    val startDate: String,
    val endDate: String,
    val archived: Boolean
)

val users = mutableListOf<User>()
val boards = mutableListOf<Board>()
val boardList = mutableListOf<BoardList>()
val cards = mutableListOf<Card>()

class DataMem : IData {
    override fun createUser(name: String, email: String): Pair<String, Int> {
        val token = UUID.randomUUID().toString()
        val newUser = User(getNextId(User::class.java), email, name, token)
        users.add(newUser)
        println(users)
        return Pair(token, newUser.idUser)
    }

    override fun getUserInfo(idUser: Int): User? {
        TODO("Not yet implemented")
    }

    override fun createBoard(name: String, description: String): Int {
        TODO("Not yet implemented")
    }

    override fun addUserToBoard(idUser: Int, idBoard: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun getBoardsFromUser(idUser: Int): List<Board> {
        TODO("Not yet implemented")
    }

    override fun getBoardInfo(idBoard: Int): Board {
        TODO("Not yet implemented")
    }

    override fun createNewListInBoard(name: String): Int {
        TODO("Not yet implemented")
    }

    override fun getListOfBoard(idBoard: Int): List<BoardList> {
        TODO("Not yet implemented")
    }

    override fun getListInfo(idBoard: Int, idList: Int): BoardList {
        TODO("Not yet implemented")
    }

    override fun createCard(name: String, description: String, endDate: String): Int {
        TODO("Not yet implemented")
    }

    override fun createCard(name: String, description: String): Int {
        TODO("Not yet implemented")
    }

    override fun getCardFromList(idBoard: Int, idList: Int): List<Card> {
        TODO("Not yet implemented")
    }

    override fun getCardInfoFromList(idBoard: Int, idList: Int, idCard: Int): Card {
        TODO("Not yet implemented")
    }

    override fun moveCard(idList: Int): Boolean {
        TODO("Not yet implemented")
    }

}

fun getNextId(clazz: Class<*>): Int {
    //println(clazz.simpleName)
    //println(User::class.simpleName)
    val nextId = when (clazz.simpleName) {
        User::class.simpleName -> if (users.isNotEmpty()) users.last().idUser.inc() else 0
        Board::class.simpleName -> if (boards.isNotEmpty()) boards.last().idBoard.inc() else 0
        BoardList::class.simpleName -> if (boardList.isNotEmpty()) boardList.last().idList.inc() else 0
        Card::class.simpleName -> if (cards.isNotEmpty()) cards.last().idCard.inc() else 0
        else -> error("Unknown object type: ${clazz.toString()::class.simpleName}")
    }
    return nextId
}