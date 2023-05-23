package pt.isel.ls.server.data.dataMem

import pt.isel.ls.server.data.dataInterfaces.Data
import pt.isel.ls.server.data.dataMem.models.BoardDataMem
import pt.isel.ls.server.data.dataMem.models.CardDataMem
import pt.isel.ls.server.data.dataMem.models.ListDataMem
import pt.isel.ls.server.data.dataMem.models.UserBoardDataMem
import pt.isel.ls.server.data.dataMem.models.UserDataMem
import pt.isel.ls.server.utils.Board
import pt.isel.ls.server.utils.BoardList
import pt.isel.ls.server.utils.Card
import pt.isel.ls.server.utils.User
import pt.isel.ls.server.utils.UserBoard

class DataMem : Data {
    override val userData = UserDataMem()
    override val boardData = BoardDataMem()
    override val userBoardData = UserBoardDataMem()
    override val listData = ListDataMem()
    override val cardData = CardDataMem()
}

val users = mutableListOf<User>(
    User(
        1,
        "alberto.tremocos@gmail.com",
        "Jose",
        "token123",
        "https://live.staticflickr.com/65535/52841364369_13521f6ef1_m.jpg"
    )
)
val boards = mutableListOf<Board>()
val usersBoards = mutableListOf<UserBoard>()
val lists = mutableListOf<BoardList>()
val cards = mutableListOf<Card>()
