package pt.isel.ls.server.data.dataInterfaces

import pt.isel.ls.server.data.dataInterfaces.models.BoardData
import pt.isel.ls.server.data.dataInterfaces.models.CardData
import pt.isel.ls.server.data.dataInterfaces.models.ListData
import pt.isel.ls.server.data.dataInterfaces.models.UserBoardData
import pt.isel.ls.server.data.dataInterfaces.models.UserData

interface Data {
    val userData: UserData
    val boardData: BoardData
    val userBoardData: UserBoardData
    val listData: ListData
    val cardData: CardData
}
