package pt.isel.ls.server.services

import pt.isel.ls.BoardList
import pt.isel.ls.server.data.cardData.IDataCard
import pt.isel.ls.server.data.listData.IDataList
import pt.isel.ls.server.data.userData.IUserData
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.isValidString

class ListServices(private val boardServices: BoardServices, private val listData: IDataList) {

    fun createList(token: String, idBoard: Int, name: String): Int {
        isValidString(name)
        boardServices.getBoard(token, idBoard)
        return listData.createList(idBoard, name)
    }

    fun getList(token: String, idList: Int): BoardList {
        val list = listData.getList(idList) ?: throw TrelloException.NotFound("BoardList")
        boardServices.getBoard(token, list.idBoard)
        return list
    }

    fun getListsOfBoard(token: String, idBoard: Int): List<BoardList> {
        boardServices.getBoard(token, idBoard)
        return listData.getListsOfBoard(idBoard)
    }
}