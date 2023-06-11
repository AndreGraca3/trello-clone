package pt.isel.ls.server.services

import pt.isel.ls.server.*
import pt.isel.ls.server.data.transactionManager.executor.DataExecutor
import pt.isel.ls.server.data.dataInterfaces.models.BoardData
import pt.isel.ls.server.data.dataInterfaces.models.CardData
import pt.isel.ls.server.data.dataInterfaces.models.ListData
import pt.isel.ls.server.data.dataInterfaces.models.UserBoardData
import pt.isel.ls.server.data.dataInterfaces.models.UserData
import pt.isel.ls.server.exceptions.INVAL_PARAM
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.validateString

class BoardServices(
    private val userData: UserData,
    private val boardData: BoardData,
    private val userBoardData: UserBoardData,
    private val listData: ListData,
    private val cardData: CardData,
    private val dataExecutor: DataExecutor
) {

    fun createBoard(token: String, name: String, description: String): Int {
        validateString(name, "name")
        validateString(description, "description")

        return dataExecutor.execute { con ->
            val idUser = userData.getUser(token, con).idUser
            boardData.createBoard(idUser, name, description, con).also {
                userBoardData.addUserToBoard(idUser, it, con)
            }
        }
    }

    fun getBoard(token: String, idBoard: Int): BoardDetailed {
        return dataExecutor.execute { con ->
            val idUser = userData.getUser(token, con).idUser
            userBoardData.checkUserInBoard(idUser, idBoard, con)
            val board = boardData.getBoard(idBoard, con)
            val lists = listData.getListsOfBoard(idBoard, con)

            val detailedLists = lists.map { l ->
                ListDetailed(
                    l.idList,
                    l.idBoard,
                    l.name,
                    cardData.getCardsFromList(l.idList, idBoard, con)
                )
            }

            val archivedCards = cardData.getArchivedCards(idBoard, con)

            BoardDetailed(
                idBoard,
                board.name,
                board.description,
                board.primaryColor,
                board.secondaryColor,
                detailedLists,
                archivedCards
            )
        }
    }

    fun getBoardsFromUser(token: String, limit: Int?, skip: Int?, name: String?, numLists: Int?): TotalBoards {
        val name = name ?: ""

        return dataExecutor.execute {
            val idUser = userData.getUser(token, it).idUser
            val count = userBoardData.getBoardCountFromUser(idUser, name, numLists, it)
            val boards = boardData.getBoardsFromUser(
                idUser,
                if (limit != null && limit < 0) null else limit,
                if (skip != null && skip < 0) null else skip,
                name,
                numLists,
                it
            )
            TotalBoards(count, boards)
        }
    }

    fun addUserToBoard(token: String, newUserEmail: String, idBoard: Int) : UserProfile {
        return dataExecutor.execute {
            validateString(newUserEmail, "email")
            verifyEmail(newUserEmail)
            val idUserOwner = userData.getUser(token, it).idUser
            val userToAdd = userData.getUserByEmail(newUserEmail, it) // check if user to add exists
            userBoardData.checkUserInBoard(idUserOwner, idBoard, it)
            //kotlin.runCatching { userBoardData.addUserToBoard(idUserToAdd, idBoard, it) }
            try {
                userBoardData.addUserToBoard(userToAdd.idUser, idBoard, it)
            } catch (_: Exception) {
                UserProfile(userToAdd.avatar, userToAdd.name)
            }
        }
    }

    fun getUsersFromBoard(token: String, idBoard: Int): List<User> {
        return dataExecutor.execute {
            val idUser = userData.getUser(token, it).idUser
            userBoardData.checkUserInBoard(idUser, idBoard, it)
            userData.getUsers(
                idBoard,
                it
            )
        }
    }

    private fun verifyEmail(email: String) {
        if (!Regex("@").containsMatchIn(email)) throw TrelloException.IllegalArgument("$INVAL_PARAM $email")
    }
}
