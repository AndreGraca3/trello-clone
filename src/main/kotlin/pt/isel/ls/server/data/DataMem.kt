package pt.isel.ls.server.data

import pt.isel.ls.Board
import pt.isel.ls.BoardList
import pt.isel.ls.Card
import pt.isel.ls.User
import pt.isel.ls.server.data.boardData.BoardDataMem
import pt.isel.ls.server.data.cardData.CardDataMem
import pt.isel.ls.server.data.listData.ListDataMem
import pt.isel.ls.server.data.userData.UserDataMem

class DataMem(): Data {
    override  val users = mutableListOf<User>()
    override val boards = mutableListOf<Board>()
    override val lists = mutableListOf<BoardList>()
    override val cards = mutableListOf<Card>()

    override val userData = UserDataMem()
    override val boardData = BoardDataMem()
    override val listData = ListDataMem()
    override val cardData = CardDataMem()
}