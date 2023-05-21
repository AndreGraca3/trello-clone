package pt.isel.ls.server.services

import pt.isel.ls.server.data.dataInterfaces.DataExecutor
import pt.isel.ls.server.data.dataInterfaces.models.BoardData
import pt.isel.ls.server.data.dataInterfaces.models.CardData
import pt.isel.ls.server.data.dataInterfaces.models.ListData
import pt.isel.ls.server.data.dataInterfaces.models.UserBoardData
import pt.isel.ls.server.data.dataInterfaces.models.UserData
import pt.isel.ls.server.utils.BoardHTML
import pt.isel.ls.server.utils.CardHTML
import pt.isel.ls.server.utils.ListHTML
import pt.isel.ls.server.utils.TotalBoards
import pt.isel.ls.server.utils.User
import pt.isel.ls.server.utils.isValidString

class BoardServices(
    private val userData: UserData,
    private val boardData: BoardData,
    private val userBoardData: UserBoardData,
    private val listData: ListData,
    private val cardData: CardData,
    private val dataExecutor: DataExecutor<Any>
) {

    fun createBoard(token: String, name: String, description: String): Int {
        isValidString(name, "name")
        isValidString(description, "description")

        return dataExecutor.execute { con ->
            val idUser = userData.getUser(token, con).idUser
            boardData.createBoard(idUser, name, description, con).also {
                userBoardData.addUserToBoard(idUser, it, con)
            }
        } as Int
    }

    fun getBoard(token: String, idBoard: Int): BoardHTML {
        return dataExecutor.execute { con ->
            val idUser = userData.getUser(token, con).idUser
            userBoardData.checkUserInBoard(idUser, idBoard, con)
            val board = boardData.getBoard(idBoard, con)
            val lists = listData.getListsOfBoard(idBoard, con)

            val detailedLists = lists.map { l ->
                ListHTML(
                    l.idList, l.idBoard, l.name,
                    cardData.getCardsFromList(l.idList, l.idBoard, con)
                        .map { CardHTML(it.idCard, it.idList, it.idBoard, it.name, it.idx, it.archived) }
                )
            }
            BoardHTML(idBoard, board.name, board.description, detailedLists)
        } as BoardHTML
    }

    fun getBoardsFromUser(token: String, limit: Int?, skip: Int?, name: String?, numLists: Int?): TotalBoards {
        val name = name ?: ""

        return dataExecutor.execute {
            val idUser = userData.getUser(token, it).idUser
            val count = userBoardData.getBoardCountFromUser(idUser, name, numLists)
            val boards = boardData.getBoardsFromUser(
                idUser,
                if(limit != null && limit < 0) null else limit,
                if(skip != null && skip < 0) null else skip,
                name,
                numLists,
                it
            )
            TotalBoards(count, boards)
        } as TotalBoards
    }

    fun addUserToBoard(token: String, idNewUser: Int, idBoard: Int) {
        return dataExecutor.execute {
            val idUser = userData.getUser(token, it).idUser
            userData.getUser(idNewUser, it) // check if user to add exists
            userBoardData.checkUserInBoard(idUser, idBoard, it)
            userBoardData.addUserToBoard(idNewUser, idBoard, it)
        } as Unit
    }

    fun getUsersFromBoard(token: String, idBoard: Int, limit: Int?, skip: Int?): List<User> {
        return dataExecutor.execute {
            val idUser = userData.getUser(token, it).idUser
            userBoardData.checkUserInBoard(idUser, idBoard, it)
            userData.getUsers(idBoard,
                if(limit != null && limit < 0) null else limit ,
                if(skip != null && skip < 0) null else skip,
                it
            )
        } as List<User>
    }
}
