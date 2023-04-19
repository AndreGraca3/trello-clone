package pt.isel.ls.server.data.dataPostGres.dataSQL

import pt.isel.ls.server.data.dataInterfaces.ListData
import pt.isel.ls.server.utils.BoardList

class ListDataSQL : ListData {
    override val size: Int
        get() = TODO("Not yet implemented")

    override fun createList(idBoard: Int, name: String): Int {
        TODO("Not yet implemented")
    }

    override fun getList(idList: Int, idBoard: Int): BoardList {
        TODO("Not yet implemented")
    }

    override fun getListsOfBoard(idBoard: Int, limit: Int, skip: Int): List<BoardList> {
        TODO("Not yet implemented")
    }

    override fun checkListInBoard(idList: Int, idBoard: Int): BoardList {
        TODO("Not yet implemented")
    }

    override fun deleteList(idList: Int, idBoard: Int) {
        TODO("Not yet implemented")
    }

    override fun getListCount(idBoard: Int): Int {
        TODO("Not yet implemented")
    }
}
