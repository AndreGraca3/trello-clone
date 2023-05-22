package pt.isel.ls.server.services

import pt.isel.ls.server.data.dataInterfaces.DataExecutor
import pt.isel.ls.server.data.dataInterfaces.models.CardData
import pt.isel.ls.server.data.dataInterfaces.models.ListData
import pt.isel.ls.server.data.dataInterfaces.models.UserBoardData
import pt.isel.ls.server.data.dataInterfaces.models.UserData
import pt.isel.ls.server.utils.BoardList
import pt.isel.ls.server.utils.Card
import pt.isel.ls.server.utils.isValidString

class ListServices(
    private val userData: UserData,
    private val userBoardData: UserBoardData,
    private val listData: ListData,
    private val cardData: CardData,
    private val dataExecutor: DataExecutor<Any>
) {

    fun createList(token: String, idBoard: Int, name: String): Int {
        isValidString(name, "name")

        return dataExecutor.execute {
            val idUser = userData.getUser(token, it).idUser
            userBoardData.checkUserInBoard(idUser, idBoard, it)
            listData.createList(idBoard, name, it)
        } as Int
    }

    fun getList(token: String, idBoard: Int, idList: Int): BoardList {
        return dataExecutor.execute {
            val idUser = userData.getUser(token, it).idUser
            userBoardData.checkUserInBoard(idUser, idBoard, it)
            listData.getList(idList, idBoard, it)
        } as BoardList
    }

    fun getCardsFromList(token: String, idBoard: Int, idList: Int, limit: Int?, skip: Int?): List<Card> {
        return dataExecutor.execute {
            val idUser = userData.getUser(token, it).idUser
            userBoardData.checkUserInBoard(idUser, idBoard, it)
            listData.getList(idList, idBoard, it)
            cardData.getCardsFromList(idList, idBoard, it)
        } as List<Card>
    }

    fun getListsOfBoard(token: String, idBoard: Int): List<BoardList> {
        return dataExecutor.execute {
            val idUser = userData.getUser(token, it).idUser
            userBoardData.checkUserInBoard(idUser, idBoard, it)
            listData.getListsOfBoard(idBoard, it)
        } as List<BoardList>
    }

    fun deleteList(token: String, idBoard: Int, idList: Int, action: String?) {
        return dataExecutor.execute {
            val idUser = userData.getUser(token, it).idUser
            userBoardData.checkUserInBoard(idUser, idBoard, it)
            when (action) {
                "delete" -> cardData.deleteCards(idList, it)
                "archive" -> cardData.archiveCards(idBoard, idList, it)
            }
            listData.deleteList(idList, idBoard, it)
        } as Unit
    }
}
