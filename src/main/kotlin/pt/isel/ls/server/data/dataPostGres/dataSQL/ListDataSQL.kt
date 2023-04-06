package pt.isel.ls.server.data.dataPostGres.dataSQL

import pt.isel.ls.server.data.dataInterfaces.ListData
import pt.isel.ls.server.utils.BoardList

class ListDataSQL : ListData {

    override fun createList(idBoard: Int, name: String): Int {
        TODO("Not yet implemented")
    }

    override fun getList(idList: Int, idBoard: Int): BoardList {
        TODO("Not yet implemented")
    }

    override fun getListsOfBoard(idBoard: Int): List<BoardList> {
        TODO("Not yet implemented")
    }

    override fun checkListInBoard(idList: Int, idBoard: Int): BoardList {
        TODO("Not yet implemented")
    }

    override fun deleteList(idList: Int, idBoard: Int) {
        TODO("Not yet implemented")
    }
}
