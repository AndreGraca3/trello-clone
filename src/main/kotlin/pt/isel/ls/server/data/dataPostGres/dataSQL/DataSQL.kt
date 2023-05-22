package pt.isel.ls.server.data.dataPostGres.dataSQL

import pt.isel.ls.server.data.dataInterfaces.Data
import pt.isel.ls.server.data.dataPostGres.dataSQL.models.BoardDataSQL
import pt.isel.ls.server.data.dataPostGres.dataSQL.models.CardDataSQL
import pt.isel.ls.server.data.dataPostGres.dataSQL.models.ListDataSQL
import pt.isel.ls.server.data.dataPostGres.dataSQL.models.UserBoardDataSQL
import pt.isel.ls.server.data.dataPostGres.dataSQL.models.UserDataSQL

class DataSQL : Data {
    override val userData = UserDataSQL()
    override val boardData = BoardDataSQL()
    override val userBoardData = UserBoardDataSQL()
    override val listData = ListDataSQL()
    override val cardData = CardDataSQL()
}
