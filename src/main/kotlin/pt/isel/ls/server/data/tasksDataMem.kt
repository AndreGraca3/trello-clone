package pt.isel.ls.server.data

import pt.isel.ls.Board
import pt.isel.ls.BoardList
import pt.isel.ls.Card
import pt.isel.ls.User
import java.time.LocalDate
import java.util.UUID

val users = mutableListOf<User>()
val boards = mutableListOf<Board>()
val lists = mutableListOf<BoardList>()
val cards = mutableListOf<Card>()

class DataMem : IData {
    override fun createUser(name: String, email: String): Pair<Int, String> {
        val token = UUID.randomUUID().toString()
        val newUser = User(getNextId(User::class.java), email, name, token)
        users.add(newUser)
        return Pair(newUser.idUser, token)
    }

    override fun getUserInfo(idUser: Int): User? {
        return users.find { it.idUser == idUser }
    }

    override fun getUserByEmail(email: String): Int? {
        return users.find { it.email == email }?.idUser
    }

    override fun getIdUserByToken(token: String?): Int?{
        return users.find{ it.token == token }?.idUser
    }

    override fun createBoard(idUser: Int, name: String, description: String): Int {
        val newBoard = Board(getNextId(Board::class.java), name, description, mutableListOf<Int>())
        addUserToBoard(idUser, newBoard)
        boards.add(newBoard)
        return newBoard.idBoard
    }

    override fun addUserToBoard(idUser: Int, board: Board) {
        board.idUsers.add(idUser)
    }

    override fun checkIfUserExistsInBoard(idUser: Int,idBoard: Int) : Boolean {
        return boards.any { it.idUsers.contains(idUser) && it.idBoard==idBoard }
    }
    override fun getBoardByName(name: String): Board? {
        return boards.find { it.name == name }
    }

    override fun getBoardsFromUser(idUser: Int): List<Board> {
        return boards.filter { it.idUsers.any { id -> id == idUser } }
    }

    override fun getBoardInfo(idBoard: Int): Board? {
        return boards.find { it.idBoard == idBoard }
    }

    override fun createNewListInBoard(idBoard: Int, name: String): Int {
        val newBoardList = BoardList(getNextId(BoardList::class.java), idBoard, name)
        lists.add(newBoardList)
        return newBoardList.idList
    }

    override fun getListsOfBoard(idBoard: Int): List<BoardList> {
        return lists.filter { it.idBoard == idBoard }
    }

    override fun getListInfo(idList: Int): BoardList? {
        return lists.find { it.idList == idList }
    }

    override fun createCard(idList: Int, name: String, description: String, endDate: String?): Int {
        val newCard = Card(getNextId(Card::class.java), idList, name, description, LocalDate.now().toString(),endDate, false)
        cards.add(newCard)
        return newCard.idCard
    }

    override fun getCardsFromList(idList: Int): List<Card> {
        return cards.filter { it.idList == idList }
    }

    override fun getCardInfo(idCard: Int): Card? {
        return cards.find { it.idCard == idCard }
    }

    override fun moveCard(card: Card, idListDst: Int) {
        card.idList = idListDst
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