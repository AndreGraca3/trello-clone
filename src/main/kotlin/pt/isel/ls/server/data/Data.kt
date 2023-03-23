package pt.isel.ls.server.data

import pt.isel.ls.Board
import pt.isel.ls.BoardList
import pt.isel.ls.Card
import pt.isel.ls.User
import pt.isel.ls.server.data.boardData.IDataBoard
import pt.isel.ls.server.data.cardData.IDataCard
import pt.isel.ls.server.data.listData.IDataList
import pt.isel.ls.server.data.userData.IUserData

interface Data {
    val users: MutableList<User>
    val boards: MutableList<Board>
    val lists: MutableList<BoardList>
    val cards: MutableList<Card>

    val userData: IUserData
    val boardData: IDataBoard
    val listData: IDataList
    val cardData: IDataCard
}