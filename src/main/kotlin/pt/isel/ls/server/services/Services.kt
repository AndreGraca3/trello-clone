package pt.isel.ls.server.services

import pt.isel.ls.Board
import pt.isel.ls.BoardList
import pt.isel.ls.Card
import pt.isel.ls.server.data.*
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.isValidString
import java.time.LocalDate

open class Services(private val data : Data) {   //could it be sealed class and then each services inherit from it?

    val userServices = UserServices(data.userData)

    val boardServices = BoardServices(userServices, data.boardData)

    val listServices = ListServices(boardServices, data.listData)

    val cardServices = CardServices(listServices, data.cardData)
}