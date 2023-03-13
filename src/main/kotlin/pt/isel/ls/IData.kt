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

    fun addUserToBoard(idUser: Int, idBoard: Int): Boolean //Int? (idBoard) ou Board?

    fun getBoardsFromUser(idUser: Int): List<Board>

    fun getBoardInfo(idBoard: Int): Board?

    /** ----------------------------
     *  List Functions
     *  ------------------------------**/

    fun createNewListInBoard(idBoard: Int, name: String): Int

    fun getListsOfBoard(idBoard: Int): List<BoardList>

    fun getListInfo(idBoard: Int, idList: Int): BoardList?

    /** ----------------------------
     *  Card Functions
     *  ------------------------------**/

    fun createCard(idList: Int, name: String, description: String, endDate: String): Int //check endDate

    fun createCard(idList: Int, name: String, description: String): Int

    fun getCardsFromList(idBoard: Int, idList: Int): List<Card>

    fun getCardInfoFromList(idBoard: Int, idList: Int, idCard: Int): Card

    fun moveCard(idList: Int): Boolean


}