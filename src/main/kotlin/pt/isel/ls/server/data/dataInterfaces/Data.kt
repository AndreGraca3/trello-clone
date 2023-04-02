package pt.isel.ls.server.data.dataInterfaces

interface Data {
    val userData: UserData
    val boardData: BoardData
    val userBoardData: UserBoardData
    val listData: ListData
    val cardData: CardData
}
