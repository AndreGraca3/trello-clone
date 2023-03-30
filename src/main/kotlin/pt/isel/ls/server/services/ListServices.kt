package pt.isel.ls.server.services

import pt.isel.ls.server.data.checkListInBoard
import pt.isel.ls.server.data.checkUserInBoard
import pt.isel.ls.server.data.getUser
import pt.isel.ls.server.data.listData.DataList
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.BoardList
import pt.isel.ls.server.utils.isValidString

class ListServices(private val listData: DataList) {

    /** ------------------------------ *
     *         List Management         *
     *  ----------------------------- **/

    fun createList(token: String, idBoard: Int, name: String): Int { /** check **/
        isValidString(name)
        val idUser = getUser(token).idUser
        if(!checkUserInBoard(idUser,idBoard)) throw TrelloException.NotAuthorized() // Not sure
        return listData.createList(idBoard, name)
    }

    fun getList(token: String, idBoard: Int, idList: Int): BoardList { /** check **/
        val idUser = getUser(token).idUser
        if(!checkUserInBoard(idUser,idBoard)) throw TrelloException.NotAuthorized()
        return listData.getList(idList, idBoard) ?: throw TrelloException.NotFound("BoardList")
    }

    fun getListsOfBoard(token: String, idBoard: Int): List<BoardList> { /** check **/
        val idUser = getUser(token).idUser
        if(!checkUserInBoard(idUser,idBoard)) throw TrelloException.NotAuthorized()
        return listData.getListsOfBoard(idBoard)
    }
}