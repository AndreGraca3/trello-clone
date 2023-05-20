package pt.isel.ls.server.services

import pt.isel.ls.server.data.dataInterfaces.BoardData
import pt.isel.ls.server.data.dataInterfaces.CardData
import pt.isel.ls.server.data.dataInterfaces.ListData
import pt.isel.ls.server.data.dataInterfaces.UserBoardData
import pt.isel.ls.server.data.dataInterfaces.UserData
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.exceptions.map
import pt.isel.ls.server.utils.BoardHTML
import pt.isel.ls.server.utils.BoardWithLists
import pt.isel.ls.server.utils.CardHTML
import pt.isel.ls.server.utils.ListHTML
import pt.isel.ls.server.utils.TotalBoards
import pt.isel.ls.server.utils.User
import pt.isel.ls.server.utils.checkPaging
import pt.isel.ls.server.utils.isValidString
import java.sql.SQLException

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
        //boardData.checkBoardName(name)
        val idUser = userData.getUser(token).idUser
        //userBoardData.addUserToBoard(idUser, idBoard)
        try{
            return boardData.createBoard(idUser, name, description)
        } catch (ex: SQLException) {
            val trelloException = map[ex.sqlState] ?: throw Exception() // upper modules will see this exception has a bad request.
            throw trelloException("board")
        }
    }

    fun getBoard(token: String, idBoard: Int): BoardHTML {
        val idUser = userData.getUser(token).idUser
        userBoardData.checkUserInBoard(idUser, idBoard)
        /*val board = boardData.getBoard(idBoard)
        val countList = listData.getListCount(idBoard)
        val lists = listData.getListsOfBoard(idBoard, countList, 0)
        val cards = lists.map {
            val countCard = cardData.getCardCount(idBoard, it.idList)
            cardData.getCardsFromList(it.idList, it.idBoard, countCard, 0)
        }
        val listsHTML = mutableListOf<ListHTML>()
        for (i in lists.indices) {
            val currCards = cards[i].map { CardHTML(it.idCard, it.idList, it.idBoard, it.name, it.idx, it.archived) }
            val currList = lists[i]
            listsHTML.add(ListHTML(currList.idList, currList.idBoard, currList.name, currCards))
        }*/
        val boardsSQL = boardData.getBoard(idBoard)
        val listsHtml = mutableListOf<ListHTML>()
        var cardsHtml = mutableListOf<CardHTML>()

        for (i in boardsSQL.indices) {
            if (boardsSQL[i].idCard == null) {
                listsHtml.add(ListHTML(boardsSQL[i].idList, idBoard, boardsSQL[i].listName, emptyList()))
            } else {
                val currListId = boardsSQL[i].idList
                cardsHtml.add(
                    CardHTML(
                        boardsSQL[i].idCard!!,
                        currListId,
                        idBoard,
                        boardsSQL[i].cardName!!,
                        boardsSQL[i].cardIdx!!,
                        boardsSQL[i].cardArchived!!
                    )
                )
                if(i + 1 >= boardsSQL.size || currListId != boardsSQL[i + 1].idList ) {
                    listsHtml.add(ListHTML(currListId, idBoard, boardsSQL[i].listName, cardsHtml))
                    cardsHtml = mutableListOf()
                }
            }
        }
        return BoardHTML(idBoard, boardsSQL.first().boardName, boardsSQL.first().boardDescription, listsHtml)
    }

    fun getBoardsFromUser(token: String, limit: Int?, skip: Int?, name: String?, numLists: Int?): TotalBoards {
        val idUser = userData.getUser(token).idUser
        val name = name ?: ""
        val numLists = numLists ?: 0
        val count = userBoardData.getBoardCountFromUser(idUser, name, numLists)

        val boards = boardData.getBoardsFromUser(
            idUser,
            if(limit != null && limit < 0) null else limit,
            if(skip != null && skip < 0) null else skip,
            name,
            numLists
        )
        return TotalBoards(count, boards)
    }

    fun addUserToBoard(token: String, idNewUser: Int, idBoard: Int) {
        val idUser = userData.getUser(token).idUser
        userBoardData.checkUserInBoard(idUser, idBoard)
        userData.getUser(idNewUser) // check if user to add exists
        /*try {
            userBoardData.checkUserInBoard(idNewUser, idBoard) // this throws exception
        } catch (e: Exception) {
            userBoardData.addUserToBoard(idNewUser, idBoard)
        }*/
        userBoardData.addUserToBoard(idNewUser, idBoard)
    }

    fun getUsersFromBoard(token: String, idBoard: Int, limit: Int?, skip: Int?): List<User> {
        val idUser = userData.getUser(token).idUser
        userBoardData.checkUserInBoard(idUser, idBoard)
        //val userIds = userBoardData.getIdUsersFromBoard(idBoard)
        //val count = userBoardData.getUserCountFromBoard(idBoard)
        //val pairPaging = checkPaging(count, limit, skip)
        return userData.getUsers(idBoard,
            if(limit != null && limit < 0) null else limit ,
            if(skip != null && skip < 0) null else skip
        )
    }
}
