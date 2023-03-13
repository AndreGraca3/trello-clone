package pt.isel.ls

import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.util.UUID

@Serializable
data class User(val idUser: Int, val email: String, val name: String, val token: String)

@Serializable
data class Board(val idBoard: Int = 0, val name: String, val description: String, val idUsers: MutableList<Int>)

@Serializable
data class BoardList(val idList: Int = 0, val idBoard: Int, val name: String)

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
val lists = mutableListOf<BoardList>()
val cards = mutableListOf<Card>()

class DataMem : IData {
    override fun createUser(name: String, email: String): Pair<String, Int> { //check
        val token = UUID.randomUUID().toString()
        val newUser = User(getNextId(User::class.java), email, name, token)
        users.add(newUser)
        return Pair(token, newUser.idUser)
    }

    override fun getUserInfo(idUser: Int): User? { //check
        return users.find { it.idUser == idUser }
    }

    override fun createBoard(idUser: Int, name: String, description: String): Int { // check
        val list = mutableListOf<Int>()
        list.add(idUser) // adds user that created the board.
        val newBoard = Board(getNextId(Board::class.java), name, description, list)
        boards.add(newBoard)
        return newBoard.idBoard
    }

    override fun addUserToBoard(idUser: Int, board : Board) { // check
        board.idUsers.add(idUser)
    }

    override fun getBoardsFromUser(idUser: Int): List<Board> { // check
        val boardsFromUsers = mutableListOf<Board>()
        boards.forEach { it ->
            if (it.idUsers.find { it == idUser } != null) {
                    boardsFromUsers.add(it)
            }
        }
        return boardsFromUsers
    }

    override fun getBoardInfo(idBoard: Int): Board? { // check
        return boards.find { it.idBoard == idBoard }
    }

    override fun createNewListInBoard(idBoard: Int, name: String): Int { // check
        val newBoardList = BoardList(getNextId(BoardList::class.java), idBoard, name)
        lists.add(newBoardList)
        return newBoardList.idList
    }

    override fun getListsOfBoard(idBoard: Int): List<BoardList> { // check
        val listsFromBoard = mutableListOf<BoardList>()
        lists.forEach {
            if(it.idBoard == idBoard){
                listsFromBoard.add(it)
            }
        }
        return listsFromBoard
    }

    override fun getListInfo(idList: Int): BoardList? {
        return lists.find { it.idList == idList }
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
    val nextId = when (clazz.simpleName) {
        User::class.simpleName -> if (users.isNotEmpty()) users.last().idUser.inc() else 0
        Board::class.simpleName -> if (boards.isNotEmpty()) boards.last().idBoard.inc() else 0
        BoardList::class.simpleName -> if (lists.isNotEmpty()) lists.last().idList.inc() else 0
        Card::class.simpleName -> if (cards.isNotEmpty()) cards.last().idCard.inc() else 0
        else -> error("Unknown object type: ${clazz.toString()::class.simpleName}")
    }
    return nextId
}