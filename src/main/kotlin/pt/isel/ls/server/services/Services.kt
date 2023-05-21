package pt.isel.ls.server.services

import pt.isel.ls.server.data.dataInterfaces.Data
import pt.isel.ls.server.data.dataInterfaces.DataExecutor

class Services(data: Data, dataExecutor: DataExecutor<Any>) {
    val userServices = UserServices(data.userData, dataExecutor)
    val boardServices = BoardServices(data.userData, data.boardData, data.userBoardData, data.listData, data.cardData, dataExecutor)
    val listServices = ListServices(data.userData, data.userBoardData, data.listData, data.cardData, dataExecutor)
    val cardServices = CardServices(data.userData, data.userBoardData, data.listData, data.cardData, dataExecutor)
}
