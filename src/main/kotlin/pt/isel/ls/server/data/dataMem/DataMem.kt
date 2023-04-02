package pt.isel.ls.server.data.dataMem

import pt.isel.ls.server.data.dataInterfaces.Data

class DataMem : Data {
    override val userData = UserDataMem()
    override val boardData = BoardDataMem()
    override val userBoardData = UserBoardDataMem()
    override val listData = ListDataMem()
    override val cardData = CardDataMem()
}
