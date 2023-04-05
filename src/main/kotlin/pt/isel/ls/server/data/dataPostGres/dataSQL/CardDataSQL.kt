package pt.isel.ls.server.data.dataPostGres.dataSQL

import pt.isel.ls.server.data.dataInterfaces.CardData
import pt.isel.ls.server.data.dataPostGres.statements.CardStatements
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.Card
import pt.isel.ls.server.utils.setup

class CardDataSQL : CardData {

    override fun createCard(idList: Int, idBoard: Int, name: String, description: String, endDate: String?): Int {
        val dataSource = setup()
        val insertStmtCard = CardStatements.createCardCMD(idList, idBoard, name, description, endDate)
        var idCard: Int
        // how to get idCard since name isn't unique?
        // val selectStmt = CardStatements.getCardByNameCMD(name)

        dataSource.connection.use {
            it.autoCommit = false
            it.prepareStatement(insertStmtCard).executeUpdate()

            TODO()

            it.autoCommit = true
        }
        return idCard
    }

    override fun getCardsFromList(idList: Int, idBoard: Int): List<Card> {
        val dataSource = setup()
        val selectStmt = CardStatements.getCardsFromListCMD(idList, idBoard)
        val cards = mutableListOf<Card>()

        dataSource.connection.use {
            it.autoCommit = false
            val res = it.prepareStatement(selectStmt).executeQuery()

            while (res.next()) {
                if (res.row == 0) return emptyList()    // test if this works both in here and in BoardSQL
                cards.add(
                    Card(
                        res.getInt("idCard"),
                        res.getInt("idList"),
                        res.getInt("idBoard"),
                        res.getString("name"),
                        res.getString("description"),
                        res.getString("startDate"),
                        res.getString("endDate"),
                        res.getBoolean("archived")
                    )
                )
            }
        }
        return cards
    }

    override fun getCard(idCard: Int, idList: Int, idBoard: Int): Card {
        val dataSource = setup()
        val selectStmt = CardStatements.getCardCMD(idCard, idList, idBoard)
        lateinit var name: String
        lateinit var description: String
        lateinit var startDate: String
        lateinit var endDate: String
        var archived: Boolean

        dataSource.connection.use {
            it.autoCommit = false
            val res = it.prepareStatement(selectStmt).executeQuery()
            res.next()

            if (res.row == 0) throw TrelloException.NotFound("Card")

            name = res.getString("name")
            description = res.getString("description")
            startDate = res.getString("startDate")
            endDate = res.getString("endDate")
            archived = res.getBoolean("archived")

            it.autoCommit = true
        }
        return Card(idCard, idList, idBoard, name, description, startDate, endDate, archived)
    }

    override fun moveCard(idCard: Int, idListNow: Int, idBoard: Int, idListDst: Int) {
        val dataSource = setup()
        val selectStmt = CardStatements.getCardCMD(idCard, idListNow, idBoard)
        val updateStmtCard = CardStatements.moveCardCMD(idCard, idListNow, idBoard, idListDst)

        dataSource.connection.use {
            it.autoCommit = false
            val res = it.prepareStatement(selectStmt).executeQuery()
            res.next()

            if (res.row == 0) throw TrelloException.NotFound("Card")

            it.prepareStatement(updateStmtCard).executeUpdate()

            it.autoCommit = true
        }
    }
}
