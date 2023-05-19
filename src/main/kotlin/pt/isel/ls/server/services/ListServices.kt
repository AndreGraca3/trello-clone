package pt.isel.ls.server.services

import pt.isel.ls.server.data.dataInterfaces.CardData
import pt.isel.ls.server.data.dataInterfaces.ListData
import pt.isel.ls.server.data.dataInterfaces.UserBoardData
import pt.isel.ls.server.data.dataInterfaces.UserData
import pt.isel.ls.server.utils.BoardList
import pt.isel.ls.server.utils.Card
import pt.isel.ls.server.utils.checkPaging
import pt.isel.ls.server.utils.isValidString

class ListServices(
    private val userData: UserData,
    private val userBoardData: UserBoardData,
    private val listData: ListData,
    private val cardData: CardData
) {

    /** ------------------------------ *
     *         List Management         *
     *  ----------------------------- **/

    fun createList(token: String, idBoard: Int, name: String): Int {
        isValidString(name, "name")
        val idUser = userData.getUser(token).idUser
        userBoardData.checkUserInBoard(idUser, idBoard) // App logic not Data logic.
        return listData.createList(idBoard, name)
    }

    fun getList(token: String, idBoard: Int, idList: Int): BoardList {
        val idUser = userData.getUser(token).idUser
        userBoardData.checkUserInBoard(idUser, idBoard) // App logic not Data logic.
        return listData.getList(idList, idBoard)
    }

    fun getCardsFromList(token: String, idBoard: Int, idList: Int, limit: Int?, skip: Int?): List<Card> {
        val idUser = userData.getUser(token).idUser
        userBoardData.checkUserInBoard(idUser, idBoard)
        listData.getList(idList, idBoard)
        //val count = cardData.getCardCount(idBoard, idList)
        //val pairPaging = checkPaging(count, limit, skip)
        return cardData.getCardsFromList(
            idList,
            idBoard,
            if(limit != null && limit < 0) null else limit ,
            if(skip != null && skip < 0) null else skip
        )
    }

    fun getListsOfBoard(token: String, idBoard: Int, limit: Int?, skip: Int?): List<BoardList> {
        val idUser = userData.getUser(token).idUser
        userBoardData.checkUserInBoard(idUser, idBoard)
        //val count = listData.getListCount(idBoard)
        //val pairPaging = checkPaging(count, limit, skip)
        return listData.getListsOfBoard(idBoard,
            if(limit != null && limit < 0) null else limit ,
            if(skip != null && skip < 0) null else skip
        )
    }

    fun deleteList(token: String, idBoard: Int, idList: Int, action: String?) {
        val idUser = userData.getUser(token).idUser
        userBoardData.checkUserInBoard(idUser, idBoard)
        when(action){
            "D" -> cardData.deleteCards(idList)
            "A" -> cardData.archiveCards(idList)
        }
        listData.deleteList(idList, idBoard)
    }
}
