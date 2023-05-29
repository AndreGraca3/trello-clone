import {boardFunc} from "../HTML/DSL/listeners/boardFuncs.js";
import {createList} from "../HTML/DSL/listeners/listFuncs.js";
import {LIMIT_INITIAL_VALUE, mainContent, MAX_BOARDS_DISPLAY} from "../config/storage.js";
import {button, createElement, div, h1, select} from "../HTML/components/components.js";
import {
    createHTMLBoard,
    createHTMLList,
    createPaginationButtons,
    createRows,
    createSearchBar
} from "../HTML/DSL/components/modelComponents.js";
import {
    darkerColor,
    getBoardColor,
    getLimitSelectorOptions,
    getNewBoardsPath,
    visitBoard
} from "../HTML/DSL/modelAuxs.js";
import {archivedDropdown, usersDropdown} from "../HTML/DSL/dropdowns/modelDropdowns.js";
import {cardModalHTML, listModalHTML} from "../HTML/DSL/modals/modals.js";
import boardData from "../data/boardData.js";


async function getBoards(args) {
    document.title = "OurTrello | Boards"

    h1("My Boards")

    const res = await boardData.getBoards(args.skip, args.limit, args.name, args.numLists)

    const boards = res.boards

    // Fix skip and limit values
    if (!args.skip) args.skip = 0
    if (!args.limit) args.limit = LIMIT_INITIAL_VALUE
    if (args.skip < 0) args.skip = Math.max(0, args.skip)
    if (args.skip > res.totalBoards) args.skip = Math.min(res.totalBoards - args.limit + 1, args.skip)
    args.skip = parseInt(args.skip)
    args.limit = parseInt(args.limit)

    document.location = getNewBoardsPath(args.skip, args.limit, args.name, args.numLists)

    const boardCards = boards.map(board =>
        createHTMLBoard(board.name, board.description, board.numLists, () =>
            boardFunc(board), getBoardColor(board.idBoard))
    )

    // Pagination limit selector
    const limitOptions = getLimitSelectorOptions(MAX_BOARDS_DISPLAY, 5, args.limit)

    const options = limitOptions.map(it => createElement("option", it))
    const selectHtml = select(null, [], null, ...options)
    selectHtml.value = args.limit
    selectHtml.addEventListener("change", (ev) =>
        document.location = getNewBoardsPath(0, ev.target.value, args.name, args.numLists)
    )

    div(null, ["paginationContainer"], null,
        div("Boards per Page: ", ["pagination-control"], null, selectHtml),
        createPaginationButtons(args.skip, args.limit, res.totalBoards, args.name, args.numLists),
        createSearchBar(args.name, args.numLists)
    )

    // Boards
    createRows(boardCards, MAX_BOARDS_DISPLAY)
}


async function getBoard(args) {
    const id = args.idBoard

    const color = getBoardColor(id)
    mainContent.style.background = `linear-gradient(135deg, ${darkerColor(color)}, ${color})`

    const board = await boardData.getBoard(id)
    document.title = `OurTrello | ${board.name}`
    visitBoard(board)

    div(null, ["board-header"], null,
        h1(board.description, ["board-desc"]),
        div(null, ["board-buttons"], null,
            await usersDropdown(board.idBoard),
            await archivedDropdown(board)
        )
    )

    const listCards = board.lists.map(list => createHTMLList(list))
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