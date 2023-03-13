package pt.isel.ls

import java.lang.Exception

sealed class ServiceException(message: String) : Exception(message) {

    class BoardNotFoundException(idBoard: Int) :
        ServiceException("Board with idBoard=$idBoard not found.")

    class UserNotFoundException(idUser: Int) :
        ServiceException("User with idUser=$idUser not found.")

    class UserCreationException(email: String) :
        ServiceException("Error creating user with email=$email (already been taken).")

    class BoardCreationException(idUser: Int) :
        ServiceException("Error creating board with idUser=$idUser.")

    class BoardDuplicateNameException(name : String) :
        ServiceException("Error creating board with name=$name.")
    class UserBoardsException(idUser: Int) :
        ServiceException("Boards not found with idUser=$idUser.")

    class ListCreationOnBoardException(idBoard: Int, name: String) :
        ServiceException("Error creating a list in board with idBoard=$idBoard and name=$name.")

    class BoardListsException(idBoard: Int) :
        ServiceException("Lists not found in board with idBoard=$idBoard.")

    class ListNotFoundException(idList: Int) :
        ServiceException("List not found with idList=$idList.")

    class CardCreationDateException(endDate: String) :
        ServiceException("Error creating card in list with endDate=$endDate.")

    class CardCreationException(idList: Int, name: String) :
        ServiceException("Error creating card in list with idList=$idList and name=$name.")

    class CardListException(idBoard: Int, idList: Int) :
        ServiceException("Cards not found in board with idBoard=$idBoard and idList=$idList.")

    class CardNotFoundException(idCard: Int) :
        ServiceException("Card not found with idCard=$idCard.")

    class MoveCardException(idList: Int) :
        ServiceException("Error moving card in list with idList=$idList.")

}

