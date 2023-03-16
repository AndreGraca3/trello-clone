package pt.isel.ls.server

import pt.isel.ls.Board
import pt.isel.ls.BoardList
import pt.isel.ls.Card
import pt.isel.ls.User
import pt.isel.ls.server.data.*
import pt.isel.ls.server.exceptions.TrelloException
import java.time.LocalDate

class Services(private val data: IData) {
    fun createUser(name: String, email: String): Pair<Int, String> {
        if(!Regex("@").containsMatchIn(email)) throw TrelloException.IllegalArgumentException(email)
        if(data.getUserByEmail(email) != null) throw TrelloException.IllegalArgumentException(email)
        return data.createUser(name, email)
    }

    fun getUserInfo(token: String): User {
        // decisão:
        // 1ª -> É necessário login, e só posso aceder ao meu perfil. ( optá-mos por esta )
        // 2ª -> É necessário login, e posso aceder ao perfil de qualquer usuário.
        // 3ª -> Não é necessário login, e posso aceder ao perfil de qualquer usuário.
        val user = getUserByToken(token)
        return data.getUserInfo(user.idUser) ?: throw TrelloException.NotFoundException(User::class.java.simpleName)
    }

    fun getIdUserByToken(token: String): Int {
        return data.getUserByToken(token)?.idUser ?: throw TrelloException.NotAuthorized() //created new exception edit: check if NotAuhorized as arg
    }

    fun createBoard(token: String, name: String, description: String): Int { // check
        if(data.getBoardByName(name) != null) throw TrelloException.IllegalArgumentException(name)
        val user = getUserByToken(token)
        return data.createBoard(user.idUser, name, description)
    }

    fun addUserToBoard(token: String, idUser: Int, idBoard: Int) { // check
        val owner = getUserByToken(token) // "owner" of the board.
        val boards = getBoardsFromUser(token)
        val newUser = getUserInfo(idUser) // user to be added is an already existing user
        if(boards.none { it.idBoard == idBoard }) throw TrelloException.IllegalArgumentException("idBoard") // if the board belongs to the owner
        if(checkIfUserExistsInBoard(newUser.idUser,idBoard)){
            //throw TrelloException.IllegalArgumentException(idUser.toString())
            //Visto que estamos a usar um PUT, que é idempotente, não faz sentido
            //dar exception se eu tentar adicionar o mesmo usuário.
            //idempotente => A mesma operação não causa resultados diferentes na DB.
            return
        }
        val board = getBoardInfo(token,idBoard)
        data.addUserToBoard(idUser, board)
    }

    private fun checkIfUserExistsInBoard(idUser: Int, idBoard: Int): Boolean {
        return data.checkIfUserExistsInBoard(idUser, idBoard)
    }

    fun getBoardsFromUser(token: String): List<Board> {
        // decisão :
        // 1ª -> É necessário login, e só posso aceder aos meus boards. ( optá-mos por esta )
        // 2ª -> É necessário login, e posso aceder à lista de boards de qualquer usuário.
        // 3ª -> Não é necessário login, ou seja, posso aceder à lista de boards de qualquer usuário.
        val user = getUserByToken(token)
        getUserInfo(user.idUser)
        return data.getBoardsFromUser(user.idUser)
    }

    fun getBoardInfo(token: String, idBoard: Int): Board {
        // decisão : É necessário login, e só posso aceder aos meus boards. ( optá-mos por esta )
        val user = getUserByToken(token)
        if(!checkIfUserExistsInBoard(idBoard,user.idUser))
            throw TrelloException.IllegalArgumentException(user.idUser.toString())
        return data.getBoardInfo(idBoard) ?: throw TrelloException.NotFoundException(Board::class.java.simpleName)
    }

    fun createNewListInBoard(token: String, idBoard: Int, name: String): Int {
        // id do board tem de ser verificado para ter a certeza que ele existe.
        getUserByToken(token)
        getBoardInfo(token,idBoard)
        return data.createNewListInBoard(idBoard, name)
    }

    fun getListsOfBoard(idBoard: Int): List<BoardList> {
        //getBoardInfo(idBoard)
        return data.getListsOfBoard(idBoard)
    }

    fun getListInfo(idBoard: Int, idList: Int): BoardList {
        return data.getListInfo(idBoard,idList) ?: throw TrelloException.NotFoundException(BoardList::class.java.simpleName)
    }

    private fun checkEndDate(endDate: String) {
        val endDateParsed = LocalDate.parse(endDate) // 2023-03-14
        if(endDateParsed < LocalDate.now()) throw TrelloException.IllegalArgumentException(endDate)
    }

    fun createCard(idBoard: Int,idList: Int, name: String, description: String, endDate : String): Int {
        val list = data.getListInfo(idBoard,idList)
        if(list != null){
            checkEndDate(endDate)
            return data.createCard(idList, name, description, endDate)
        }
            throw TrelloException.NotFoundException("List")
    }

    fun getCardsFromList(idBoard: Int,idList: Int): List<Card> {
        data.getListInfo(idBoard,idList)
        return data.getCardsFromList(idList)
    }

    fun getCardInfo(idCard: Int): Card {
        return data.getCardInfo(idCard) ?: throw TrelloException.NotFoundException(Card::class.java.simpleName)
    }

    fun moveCard(idCard:Int, idList: Int) {
        val card = getCardInfo(idCard)
        return data.moveCard(card,idList)
    }

    /** auxiliary functions **/
    private fun getUserByToken(token : String) : User {
        val user = data.getUserByToken(token)
        if(user == null) throw TrelloException.NotFoundException("User")
        else return user
    }
}