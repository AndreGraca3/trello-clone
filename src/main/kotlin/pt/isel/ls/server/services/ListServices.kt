package pt.isel.ls.server.services

import pt.isel.ls.server.data.dataInterfaces.ListData
import pt.isel.ls.server.data.dataInterfaces.UserBoardData
import pt.isel.ls.server.data.dataInterfaces.UserData
import pt.isel.ls.server.utils.BoardList
import pt.isel.ls.server.utils.isValidString

class ListServices(
    private val userData: UserData,
    private val userBoardData: UserBoardData,
    private val listData: ListData
) {

    /** ------------------------------ *
     *         List Management         *
     *  ----------------------------- **/

    fun createList(token: String, idBoard: Int, name: String): Int {
        /** check **/
        isValidString(name)
        val idUser = userData.getUser(token).idUser
        userBoardData.checkUserInBoard(idUser, idBoard)
        return listData.createList(idBoard, name)
    }

    fun getList(token: String, idBoard: Int, idList: Int): BoardList {
        val idUser = userData.getUser(token).idUser
        userBoardData.checkUserInBoard(idUser, idBoard)
        return listData.getList(idList, idBoard)
    }

    fun getListsOfBoard(token: String, idBoard: Int): List<BoardList> {
        val idUser = userData.getUser(token).idUser
        userBoardData.checkUserInBoard(idUser, idBoard)
        return listData.getListsOfBoard(idBoard)
    }
}
