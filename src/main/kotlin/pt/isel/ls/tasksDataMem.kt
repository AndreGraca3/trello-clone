package pt.isel.ls

import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.util.UUID

@Serializable
data class User(val idUser: Int = 0, val email: String, val name: String, val token: String = "")

@Serializable
data class Board(val idUsers: MutableList<Int>, val idBoard: Int = 0, val description: String, val name: String)

@Serializable
data class BoardList(val idBoard: Int, val idList: Int = 0, val phase: String, val name: String)

@Serializable
data class Card(
    val idList: Int,
    val idCard: Int = 0,
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
        //println(users)
        return Pair(token, newUser.idUser)
    }

    override fun getUserInfo(idUser: Int): User? {
        return users.find { it.idUser == idUser }
    }


    override fun createBoard(idUser: Int, name: String, description: String): Int {
        val list = mutableListOf<Int>()
        list.add(idUser)
        val newBoard = Board(list, getNextId(Board::class.java), description, name)
        boards.add(newBoard)
        return newBoard.idBoard
    }

    override fun addUserToBoard(idUser: Int, idBoard: Int): Boolean {
        return getBoardInfo(idBoard)!!.idUsers.add(idUser)
    }

    override fun getBoardsFromUser(idUser: Int): List<Board> {
        val boardsFromUsers = mutableListOf<Board>()
        boards.forEach { it ->
            if (it.idUsers.find { it == idUser } != null) {
                    boardsFromUsers.add(getBoardInfo(it.idBoard)!!)
            }
        }
        return boardsFromUsers
    }

    override fun getBoardInfo(idBoard: Int): Board? {
        return boards.find { it.idBoard==idBoard }
    }

    override fun createNewListInBoard(idBoard: Int, name: String): Int {
        val newBoardList = BoardList(idBoard, getNextId(BoardList::class.java), "TODO", name)
        boardList.add(newBoardList)
        return newBoardList.idList
    }

    override fun getListsOfBoard(idBoard: Int): List<BoardList> {
        val listsFromBoard = mutableListOf<BoardList>()
        boardList.forEach { it ->
            if(it.idBoard==idBoard){
                listsFromBoard.add(getListInfo(idBoard,it.idList)!!)
            }
        }
        return listsFromBoard
    }

    override fun getListInfo(idBoard: Int, idList: Int): BoardList? {
        return boardList.find { it.idBoard==idBoard && it.idList==idList }
    }

    override fun createCard(idList: Int, name: String, description: String, endDate: String): Int {
        val newCard = Card(idList, getNextId(Card::class.java), name, description, LocalDate.now().toString(),endDate, false)
        cards.add(newCard)
        return newCard.idCard
    }

    override fun createCard(idList: Int, name: String, description: String): Int {
        val newCard = Card(idList, getNextId(Card::class.java), name, description, LocalDate.now().toString(),"To be defined", false)
        cards.add(newCard)
        return newCard.idCard
    }

    override fun getCardsFromList(idBoard: Int, idList: Int): List<Card> {
        val cardList = mutableListOf<Card>()
        cards.forEach {  }
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