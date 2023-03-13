package pt.isel.ls

import kotlin.collections.*

interface IData {

    /** ----------------------------
     *  User Functions
     *  ------------------------------**/

    fun createUser(name: String, email: String): Pair<String, Int>
    fun getUserInfo(idUser: Int): User?

    /** ----------------------------
     *  Board Functions
     *  ------------------------------**/

    fun createBoard(idUser: Int, name: String, description: String): Int

    fun addUserToBoard(idUser: Int, board: Board)

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