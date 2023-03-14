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
    fun getUserInfo(idUser: Int): User?

    fun getUserByEmail(email: String) : Int?

    fun getIdUserByToken(token: String): Int?

    /** ----------------------------
     *  Board Functions
     *  ------------------------------**/

    fun createBoard(idUser: Int, name: String, description: String): Int

    fun addUserToBoard(idUser: Int, board: Board)

    fun getBoardByName(name : String) : Board?

    fun getBoardsFromUser(idUser: Int): List<Board>

    fun getBoardInfo(idBoard: Int): Board?

    /** ----------------------------
     *  List Functions
     *  ------------------------------**/

    fun createNewListInBoard(idBoard: Int, name: String): Int

    fun getListsOfBoard(idBoard: Int): List<BoardList>

    fun getListInfo(idList: Int): BoardList?

    /** ----------------------------
     *  Card Functions
     *  ------------------------------**/

    fun createCard(idList: Int, name: String, description: String, endDate: String? = null): Int //check endDate

    //fun createCard(idList: Int, name: String, description: String): Int

    fun getCardsFromList(idList: Int): List<Card>

    fun getCardInfo(idCard: Int): Card?

    fun moveCard(card : Card, idListDst: Int)

}