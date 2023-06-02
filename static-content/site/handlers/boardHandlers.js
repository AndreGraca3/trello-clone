import {createList} from "../../html/dsl/listeners/listFuncs.js";
import {LIMIT_INITIAL_VALUE, mainContent} from "../../config.js";
import {button, div, h1} from "../../html/components/elements.js";
import {
    listContainer
} from "../../html/dsl/components/modelComponents.js";
import {archivedDropdown, usersDropdown} from "../../html/dsl/dropdowns/modelDropdowns.js";
import {cardModalHTML, listModalHTML} from "../../html/dsl/modals/modals.js";
import boardData from "../../data/boardData.js";
import boardViews from "../../html/dsl/views/boardViews.js";


async function getBoards(args) {

    const res = await boardData.getBoards(args.skip, args.limit, args.name, args.numLists)

    boardViews.boardsPageView(res.boards, res.totalBoards, args.skip ?? 0, args.limit ?? LIMIT_INITIAL_VALUE)
}

async function getBoard(args) {

    const board = await boardData.getBoard(args.idBoard)

    mainContent.style.background = `linear-gradient(135deg, ${board.primaryColor}, ${board.secondaryColor})`

    boardData.visitBoard(board)

    div(null, ["board-header"], null,
        h1(board.description, ["board-desc"]),
        div(null, ["board-buttons"], null,
            await usersDropdown(board.idBoard),
            await archivedDropdown(board)
        )
    )

    const listCards = board.lists.map(list => listContainer(list))
    const createListButton = button("Add new List", ["create-list-button"])
    createListButton.addEventListener("click", () => createList(boardContainer, board))

    const boardContainer = div(null, ["board"], "boardContainer",
        ...listCards,
        createListButton
    )

    cardModalHTML()
    listModalHTML()
}

export default {
    getBoards,
    getBoard
}