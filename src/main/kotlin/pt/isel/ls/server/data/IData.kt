package pt.isel.ls.server.data

import pt.isel.ls.Board
import pt.isel.ls.BoardList
import pt.isel.ls.Card
import pt.isel.ls.User
import kotlin.collections.*

interface IData {

    /** ----------------------------
     *  User Functions
     *  ------------------------------**/

    fun createUser(name: String, email: String): Pair<Int, String>

    fun getUser(idUser: Int): User?

    fun checkEmail(email: String): Boolean

    fun getUser(token: String?): User?

    /** ----------------------------
     *  Board Functions
     *  ------------------------------**/

    fun createBoard(idUser: Int, name: String, description: String): Int

    fun addUserToBoard(idUser: Int, board: Board)

    fun getBoardByName(name: String): Board?

    fun getBoardsFromUser(idUser: Int): List<Board>

    fun getBoard(idBoard: Int): Board?

    fun checkUserInBoard(idUser: Int, idBoard: Int): Boolean

    /** ----------------------------
     *  List Functions
     *  ------------------------------**/

    fun createList(idBoard: Int, name: String): Int

    fun getList(idList: Int): BoardList?

    fun getListsOfBoard(idBoard: Int): List<BoardList>

    /** ----------------------------
     *  Card Functions
     *  ------------------------------**/

    fun createCard(idList: Int, name: String, description: String, endDate: String? = null): Int //check endDate

    fun getCardsFromList(idList: Int): List<Card>

    fun getCard(idCard: Int): Card?

    fun moveCard(card: Card, idListDst: Int)

}