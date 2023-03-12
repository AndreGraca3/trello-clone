package pt.isel.ls

import java.util.Date
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

    fun createBoard(name: String, description: String): Int

    fun addUserToBoard(idUser: Int, idBoard: Int): Boolean //Int? (idBoard) ou Board?

    fun getBoardsFromUser(idUser: Int): List<Board>

    fun getBoardInfo(idBoard: Int): Board

    /** ----------------------------
     *  List Functions
     *  ------------------------------**/

    fun createNewListInBoard(name: String): Int

    fun getListOfBoard(idBoard: Int): List<BoardList>

    fun getListInfo(idBoard: Int, idList: Int): BoardList

    /** ----------------------------
     *  Card Functions
     *  ------------------------------**/

    fun createCard(name: String, description: String, endDate: String): Int //check endDate

    fun createCard(name: String, description: String): Int

    fun getCardFromList(idBoard: Int, idList: Int): List<Card>

    fun getCardInfoFromList(idBoard: Int, idList: Int, idCard: Int): Card

    fun moveCard(idList: Int): Boolean


}