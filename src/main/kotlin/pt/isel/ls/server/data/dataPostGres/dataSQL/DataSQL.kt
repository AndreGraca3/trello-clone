package pt.isel.ls.server.data.dataPostGres.dataSQL

import pt.isel.ls.server.data.dataInterfaces.Data
import pt.isel.ls.server.data.dataPostGres.dataSQL.models.*
import pt.isel.ls.server.utils.setup

class DataSQL : Data {
    override val userData = UserDataSQL()
    override val boardData = BoardDataSQL()
    override val userBoardData = UserBoardDataSQL()
    override val listData = ListDataSQL()
    override val cardData = CardDataSQL()
}
