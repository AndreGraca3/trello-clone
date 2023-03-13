package pt.isel.ls

import java.lang.Exception

sealed class ServiceException(message: String) : Exception(message) {

    class BoardNotFoundException(idBoard: Int) :
        ServiceException("Board with idBoard=$idBoard not found.")

    class UserNotFoundException(idUser: Int) :
        ServiceException("User with idUser=$idUser not found.")

    class UserCreationException(name: String, email: String) :
        ServiceException("Error creating user with name=$name and email=$email")

    class BoardCreationException(idUser: Int, name: String) :
        ServiceException("Error creating board with idUser=$idUser and name=$name")

    class UserBoardsException(idUser: Int) :
        ServiceException("Boards not found with idUser=$idUser")

    class ListCreationOnBoardException(idBoard: Int, name: String) :
        ServiceException("Error creating a list in board with idBoard=$idBoard and name=$name")

    class BoardListsException(idBoard: Int) :
        ServiceException("Lists not found in board with idBoard=$idBoard")

    class ListNotFoundException(idList: Int) :
        ServiceException("List not found with idList=$idList")

    class CardCreationDateException(idList: Int, endDate: String) :
        ServiceException("Error creating card in list with idList=$idList and endDate=$endDate")

    class CardCreationException(idList: Int, name: String) :
        ServiceException("Error creating card in list with idList=$idList and name=$name")

    class CardListException(idBoard: Int, idList: Int) :
        ServiceException("Cards not found in board with idBoard=$idBoard and idList=$idList")

    class CardNotFoundException(idBoard: Int, idList: Int, idCard: Int) :
        ServiceException("Card not found in idBoard=$idBoard, idList=$idList and idCard=$idCard")

    class MoveCardException(idList: Int) :
        ServiceException("Error moving card in list with idList=$idList")

}

