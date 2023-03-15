package pt.isel.ls.server.exceptions

import org.http4k.core.Status
import org.http4k.core.Status.Companion.BAD_REQUEST
import java.lang.Exception
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.UNAUTHORIZED

sealed class TrelloException(message: String, val status: Status) : Exception(message) {


    class NotAuthorized() :
        TrelloException("You must be logged in to access this page.", UNAUTHORIZED)

    //check this idea later via clazz
//    class NotFoundException(typeObj: String ,idObj : Int) :
//        TrelloException("$typeObj $idObj not found.", NOT_FOUND)

    class NotFoundException(idObj : Int) :
        TrelloException("$idObj not found.", NOT_FOUND)


    class IllegalArgumentException(vararg obj: String):
        TrelloException("Invalid parameters: $obj", BAD_REQUEST)

//
//    class BoardNotFoundException(idBoard: Int) :
//        ServiceException("Board with id $idBoard not found.", NOT_FOUND)
//
//    class UserNotFoundException(idUser: Int) :
//        ServiceException("User with id $idUser not found.", NOT_FOUND)
//
//    class UserCreationException(email: String) :
//        ServiceException("Error creating user with email $email (already been taken).", BAD_REQUEST)
//
//    class InvalidEmailException(email: String) :
//        ServiceException("Error creating user with email $email (invalid email).", BAD_REQUEST)
//
//    class BoardCreationException(idUser: Int) :
//        ServiceException("Error creating board with id $idUser.", INTERNAL_SERVER_ERROR)
//
//    class BoardDuplicateNameException(name : String) :
//        ServiceException("Error creating board with name $name. (duplicate)", INTERNAL_SERVER_ERROR)
//
//    class UserBoardsException(idUser: Int) :
//        ServiceException("Boards not found with idUser $idUser.", NOT_FOUND)
//
//    class ListCreationOnBoardException(idBoard: Int, name: String) :
//        ServiceException("Error creating a list in board with idBoard $idBoard and name $name.", INTERNAL_SERVER_ERROR)
//
//    class BoardListsException(idBoard: Int) :
//        ServiceException("Lists not found in board with id $idBoard.", NOT_FOUND)
//
//    class ListNotFoundException(idList: Int) :
//        ServiceException("List not found with id $idList.", NOT_FOUND)
//
//    class CardCreationDateException(endDate: String) :
//        ServiceException("Error creating card in list with endDate $endDate.", INTERNAL_SERVER_ERROR)
//
//    class CardCreationException(idList: Int, name: String) :
//        ServiceException("Error creating card in list with id $idList and name $name.", INTERNAL_SERVER_ERROR)
//
//    class CardListException(idBoard: Int, idList: Int) :
//        ServiceException("Cards not found in board with id $idBoard and idList $idList.", NOT_FOUND)
//
//    class CardNotFoundException(idCard: Int) :
//        ServiceException("Card not found with id $idCard.", NOT_FOUND)
//
//    class MoveCardException(idList: Int) :
//        ServiceException("Error moving card in list with id $idList.", INTERNAL_SERVER_ERROR)
//
//    class UserNotFoundWithTokenException(token: String) :
//        ServiceException("User not found with token $token", NOT_FOUND)
//
//    class BoardDuplicateUserException(idUser: Int,idBoard: Int) :
//        ServiceException("Error creating board with idUser=$idUser and idBoard=$idBoard (duplicate)", BAD_REQUEST)
}

