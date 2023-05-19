package pt.isel.ls.server.data.dataPostGres.dataSQL

import pt.isel.ls.server.data.dataInterfaces.CardData
import pt.isel.ls.server.data.dataPostGres.statements.CardStatements
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.Card
import pt.isel.ls.server.utils.setup
import java.sql.Statement
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date

class CardDataSQL : CardData {

    override val size: Int get() = getSizeCount("idCard", "card")

    override fun createCard(idList: Int, idBoard: Int, name: String, description: String?, endDate: String?): Int {
        val dataSource = setup()
        val insertStmt = CardStatements.createCardCMD(idList, idBoard, name, description, endDate, getNextIdx(idList))
        var idCard: Int

        dataSource.connection.use {
            it.autoCommit = false
            val res = it.prepareStatement(insertStmt).executeQuery()
            res.next()

            idCard = res.getInt("idCard")

            it.autoCommit = true
        }
        return idCard
    }

    override fun getCardsFromList(idList: Int, idBoard: Int, limit: Int?, skip: Int?): List<Card> {
        val dataSource = setup()
        val selectStmt = CardStatements.getCardsFromListCMD(idList, idBoard, limit, skip)
        val cards = mutableListOf<Card>()

        dataSource.connection.use {
            it.autoCommit = false
            val res = it.prepareStatement(selectStmt).executeQuery()

            while (res.next()) {
                if (res.row == 0) return emptyList() // test if this works both in here and in BoardSQL
                cards.add(
                    Card(
                        res.getInt("idCard"),
                        res.getInt("idList"),
                        res.getInt("idBoard"),
                        res.getString("name"),
                        res.getString("description"),
                        res.getString("startDate"),
                        res.getString("endDate"),
                        res.getBoolean("archived"),
                        res.getInt("idx")
                    )
                )
            }
        }
        return cards.sortedBy { it.idx }
    }

    override fun getCard(idCard: Int, idBoard: Int): Card {
        val dataSource = setup()
        val selectStmt = CardStatements.getCardCMD(idCard, idBoard)
        var idList: Int
        lateinit var name: String
        var description: String?
        lateinit var startDate: String
        var endDate: String? = null
        var archived: Boolean
        var idx: Int

        dataSource.connection.use {
            it.autoCommit = false
            val res = it.prepareStatement(selectStmt).executeQuery()
            res.next()

            if (res.row == 0) throw TrelloException.NotFound("Card")

            idList = res.getInt("idList")
            name = res.getString("name")
            description = res.getString("description")
            startDate = res.getString("startDate")
            endDate = res.getString("endDate")
            archived = res.getBoolean("archived")
            idx = res.getInt("idx")
            it.autoCommit = true
        }
        return Card(idCard, idList, idBoard, name, description, startDate, endDate, archived, idx)
    }

    override fun moveCard(idCard: Int, idBoard: Int, idListDst: Int, idxDst: Int) {
        val dataSource = setup()
        val card = getCard(idCard, idBoard)
        val updateStmtCard = CardStatements.moveCardCMD(idCard, card.idList!!, idBoard, idListDst, idxDst)

        dataSource.connection.use {
            it.autoCommit = false

            val decreaseStmt = CardStatements.decreaseIdx(card.idList!!, card.idx)
            it.prepareStatement(decreaseStmt).executeUpdate()

            val increaseStmt = CardStatements.increaseIdx(idListDst, idxDst)
            it.prepareStatement(increaseStmt).executeUpdate()

            it.prepareStatement(updateStmtCard).executeUpdate()

            it.autoCommit = true
        }
    }

    override fun deleteCard(idCard: Int, idBoard: Int) {
        val dataSource = setup()
        val deleteStmt = CardStatements.deleteCard(idCard, idBoard)

        dataSource.connection.use {
            it.autoCommit = false

            val res = it.prepareStatement(deleteStmt).executeQuery()
            res.next()

            if (res.row == 0) throw TrelloException.NoContent("card")

            val idList = res.getInt("idList")
            if( idList != 0) {
                val idx = res.getInt("idx")

                val updateIdxStmt = CardStatements.decreaseIdx(idList, idx)

                it.prepareStatement(updateIdxStmt).executeUpdate()
            }

            it.autoCommit = true
        }
    }

    override fun deleteCards(idList: Int) {
        val dataSource = setup()
        val updateStmt = CardStatements.deleteCards(idList)

        dataSource.connection.use {
            it.autoCommit = false

            it.prepareStatement(updateStmt).executeUpdate()

            it.autoCommit = true
        }

    }

    override fun archiveCards(idList: Int) {
        val dataSource = setup()
        val updateStmt = CardStatements.archiveCards(idList)

        dataSource.connection.use {
            it.autoCommit = false

            it.prepareStatement(updateStmt).executeUpdate()

            it.autoCommit = true
        }
    }

    override fun getNextIdx(idList: Int): Int {
        val dataSource = setup()
        val selectStmt = CardStatements.getNextIdx(idList)
        var nextIdx: Int

        dataSource.connection.use {
            it.autoCommit = false

            val res = it.prepareStatement(selectStmt).executeQuery()
            res.next()

            nextIdx = if (res.getInt("max") == 0) {
                1
            } else {
                res.getInt("max") + 1
            }

            it.autoCommit = true
        }
        return nextIdx
    }

    override fun getCardCount(idBoard: Int, idList: Int): Int {
        val dataSource = setup()
        val selectStmt = CardStatements.getCardCount(idBoard, idList)
        var count: Int

        dataSource.connection.use {
            it.autoCommit = false

            val res = it.prepareStatement(selectStmt).executeQuery()
            res.next()

            count = res.getInt("count")

            it.autoCommit = true
        }
        return count
    }

    override fun updateCard(
        card: Card,
        archived: Boolean,
        description: String,
        endDate: String?
    ) {
        val dataSource = setup()
        val updateStmt = CardStatements.updateCard(
            card.idCard,
            card.idBoard,
            archived,
            description,
            endDate)

        dataSource.connection.use {
            it.autoCommit = false

            it.prepareStatement(updateStmt).executeUpdate()

            it.autoCommit = true
        }
    }

    private fun size(): Int {
        val dataSource = setup()
        val selectStmt = CardStatements.size()
        var count: Int

        dataSource.connection.use {
            it.autoCommit = false

            val res = it.prepareStatement(selectStmt).executeQuery()
            res.next()

            count = res.getInt("count")

            it.autoCommit = true
        }
        return count
    }
}

fun <T,R>dataBaseConnection(context: Function<T>) : R {
    val dataSource = setup()
    dataSource.connection.use {
        it.autoCommit = false
        context
        it.autoCommit = true
    }
    return 1 as R
}