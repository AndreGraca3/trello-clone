package pt.isel.ls.server.services

import pt.isel.ls.server.data.dataInterfaces.Data

class Services(data: Data) {
    val userServices = UserServices(data.userData)
    val boardServices = BoardServices(data.userData, data.boardData, data.userBoardData)
    val listServices = ListServices(data.userData, data.userBoardData, data.listData)
    val cardServices = CardServices(data.userData, data.userBoardData, data.listData, data.cardData)
}
