package pt.isel.ls.server.data.dataPostGres.dataSQL

import pt.isel.ls.server.data.dataInterfaces.CardData
import pt.isel.ls.server.utils.Card

class CardDataSQL : CardData {

    override fun createCard(idList: Int, idBoard: Int, name: String, description: String, endDate: String?): Int {
        TODO("Not yet implemented")
    }

    override fun getCardsFromList(idList: Int, idBoard: Int): List<Card> {
        TODO("Not yet implemented")
    }

    override fun getCard(idCard: Int, idList: Int, idBoard: Int): Card {
        TODO("Not yet implemented")
    }

    override fun moveCard(idCard: Int, idListNow: Int, idBoard: Int, idListDst: Int) {
        TODO("Not yet implemented")
    }
}