package pt.isel.ls.server.data.dataPostGres.dataSQL.models

import pt.isel.ls.server.data.dataInterfaces.models.CardData
import pt.isel.ls.server.data.dataPostGres.statements.CardStatements
import pt.isel.ls.server.exceptions.NOT_FOUND
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.Card
import pt.isel.ls.server.utils.setup
import java.sql.Connection

class CardDataSQL : CardData {

    override fun createCard(
        idList: Int,
        idBoard: Int,
        name: String,
        description: String?,
        endDate: String?,
        con: Connection
    ): Int {
        val insertStmt =
            CardStatements.createCardCMD(idList, idBoard, name, description, endDate, getNextIdx(idList, con))
        var idCard: Int

        val res = con.prepareStatement(insertStmt).executeQuery()
        res.next()

        idCard = res.getInt("idCard")
        return idCard
    }

    override fun getCardsFromList(idList: Int, idBoard: Int, con: Connection): List<Card> {
        val selectStmt = CardStatements.getCardsFromListCMD(idList, idBoard)
        val cards = mutableListOf<Card>()

        val res = con.prepareStatement(selectStmt).executeQuery()

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
        return cards.sortedBy { it.idx }
    }

    override fun getCard(idCard: Int, idBoard: Int, con: Connection): Card {
        val selectStmt = CardStatements.getCardCMD(idCard, idBoard)

        val res = con.prepareStatement(selectStmt).executeQuery()
        res.next()

        if (res.row == 0) throw TrelloException.NotFound("Card $NOT_FOUND")

        val idList = res.getInt("idList")
        val name = res.getString("name")
        val description = res.getString("description")
        val startDate = res.getString("startDate")
        val endDate = res.getString("endDate")
        val archived = res.getBoolean("archived")
        val idx = res.getInt("idx")
        return Card(idCard, idList, idBoard, name, description, startDate, endDate, archived, idx)
    }

    override fun moveCard(
        idCard: Int,
        idBoard: Int,
        idList: Int,
        idListDst: Int,
        idx: Int,
        idxDst: Int,
        con: Connection
    ) {
        val updateStmtCard = CardStatements.moveCardCMD(idCard, idList, idBoard, idListDst, idxDst)

        val decreaseStmt = CardStatements.decreaseIdx(idList, idx)
        con.prepareStatement(decreaseStmt).executeUpdate()

        val increaseStmt = CardStatements.increaseIdx(idListDst, idxDst)
        con.prepareStatement(increaseStmt).executeUpdate()

        con.prepareStatement(updateStmtCard).executeUpdate()
    }

    override fun decreaseIdx(idList: Int, idx: Int, con: Connection) {
        val updateStmt = CardStatements.decreaseIdx(idList, idx)
        con.prepareStatement(updateStmt).executeUpdate()
    }

    override fun deleteCard(idCard: Int, idBoard: Int, con: Connection) {
        val deleteStmt = CardStatements.deleteCard(idCard, idBoard)

        val res = con.prepareStatement(deleteStmt).executeQuery()
        res.next()

        if (res.row == 0) throw TrelloException.NoContent()

        val idList = res.getInt("idList")
        if (idList != 0) {
            val idx = res.getInt("idx")

            val updateIdxStmt = CardStatements.decreaseIdx(idList, idx)

            con.prepareStatement(updateIdxStmt).executeUpdate()
        }
    }

    override fun deleteCards(idList: Int, con: Connection) {
        val updateStmt = CardStatements.deleteCards(idList)
        con.prepareStatement(updateStmt).executeUpdate()
    }

    override fun archiveCards(idBoard: Int, idList: Int, con: Connection) {
        val updateStmt = CardStatements.archiveCards(idBoard, idList)
        con.prepareStatement(updateStmt).executeUpdate()
    }

    override fun getNextIdx(idList: Int, con: Connection): Int {
        val selectStmt = CardStatements.getNextIdx(idList)

        val res = con.prepareStatement(selectStmt).executeQuery()
        res.next()

        return if (res.getInt("max") == 0) 1 else res.getInt("max") + 1
    }

    override fun getCardCount(idBoard: Int, idList: Int, con: Connection): Int {
        val selectStmt = CardStatements.getCardCount(idBoard, idList)

        val res = con.prepareStatement(selectStmt).executeQuery()
        res.next()

        return res.getInt("count")
    }

    override fun getArchivedCards(idBoard: Int, con: Connection): List<Card> {
        val selectStmt = CardStatements.getArchivedCards(idBoard)
        val cards = mutableListOf<Card>()

        val res = con.prepareStatement(selectStmt).executeQuery()

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
        return cards
    }

    override fun updateCard(
        card: Card,
        description: String?,
        endDate: String?,
        idList: Int?,
        archived: Boolean,
        con: Connection
    ) {
        val updateStmt = CardStatements.updateCard(
            card.idCard,
            card.idBoard,
            description,
            endDate,
            idList,
            archived
        )

        con.prepareStatement(updateStmt).executeUpdate()
        val decreaseStmt = CardStatements.decreaseIdx(card.idList, card.idx)
    }
}