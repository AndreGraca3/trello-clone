package pt.isel.ls.server.services

import pt.isel.ls.server.data.dataInterfaces.BoardData
import pt.isel.ls.server.data.dataInterfaces.CardData
import pt.isel.ls.server.data.dataInterfaces.ListData
import pt.isel.ls.server.data.dataInterfaces.UserBoardData
import pt.isel.ls.server.data.dataInterfaces.UserData
import pt.isel.ls.server.utils.Board
import pt.isel.ls.server.utils.BoardHTML
import pt.isel.ls.server.utils.CardHTML
import pt.isel.ls.server.utils.ListHTML
import pt.isel.ls.server.utils.User
import pt.isel.ls.server.utils.checkPaging
import pt.isel.ls.server.utils.isValidString

class BoardServices(
    private val userData: UserData,
    private val boardData: BoardData,
    private val userBoardData: UserBoardData,
    private val listData: ListData,
    private val cardData: CardData
) {

    /** ------------------------------- *
     *         Board Management         *
     *  ------------------------------ **/

    fun createBoard(token: String, name: String, description: String): Int {
        isValidString(name, "name")
        isValidString(description, "description")
        boardData.checkBoardName(name)
        val idUser = userData.getUser(token).idUser
        val idBoard = boardData.createBoard(idUser, name, description)
        userBoardData.addUserToBoard(idUser, idBoard)
        return idBoard
    }

    fun getBoard(token: String, idBoard: Int): BoardHTML {
        val idUser = userData.getUser(token).idUser
        userBoardData.checkUserInBoard(idUser, idBoard)
        val board = boardData.getBoard(idBoard)
        val countList = listData.getListCount(idBoard)
        val lists = listData.getListsOfBoard(idBoard, countList, 0)
        val cards = lists.map {
            val countCard = cardData.getCardCount(idBoard, it.idList)
            cardData.getCardsFromList(it.idList,it.idBoard,countCard, 0)}
        val listsHTML = mutableListOf<ListHTML>()
        for(i in lists.indices) {
            val currCards = cards[i].map { CardHTML(it.idCard, it.idList, it.idBoard, it.name, it.idx)}
            val currList = lists[i]
            listsHTML.add(ListHTML(currList.idList,currList.idBoard,currList.name,currCards))
        }
        return BoardHTML(board.idBoard,board.name,board.description,listsHTML)
    }

    fun getBoardsFromUser(token: String,limit: Int?, skip: Int?): List<Board> {
        val idUser = userData.getUser(token).idUser
        val boardsIds = userBoardData.searchUserBoards(idUser)
        val count = userBoardData.getBoardCountFromUser(idUser)
        val pairPaging = checkPaging(count, limit, skip)
        return boardData.getBoardsFromUser(boardsIds,pairPaging.second,pairPaging.first) // second => "limit" and first => "skip"
    }

    fun addUserToBoard(token: String, idNewUser: Int, idBoard: Int) {
        val idUser = userData.getUser(token).idUser
        userBoardData.checkUserInBoard(idUser, idBoard)
        userData.getUser(idNewUser) // check if user to add exists
        try {
            userBoardData.checkUserInBoard(idNewUser, idBoard) // this throws exception
        } catch (e: Exception) {
            userBoardData.addUserToBoard(idNewUser, idBoard)
        }
    }

    fun getUsersFromBoard(token: String, idBoard: Int, limit: Int?, skip: Int?) : List<User> {
        val idUser = userData.getUser(token).idUser
        userBoardData.checkUserInBoard(idUser, idBoard)
        val userIds = userBoardData.getIdUsersFromBoard(idBoard)
        val count = userBoardData.getUserCountFromBoard(idBoard)
        val pairPaging = checkPaging(count, limit, skip)
        return userData.getUsers(userIds, pairPaging.second, pairPaging.first)
    }
}
