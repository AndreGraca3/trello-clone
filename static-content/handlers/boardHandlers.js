import {boardFunc} from "../HTML/DSL/listeners/boardFuncs.js";
import {createList} from "../HTML/DSL/listeners/listFuncs.js";
import {fetchReq} from "../utils/utils.js";
import {LIMIT_INITIAL_VALUE, mainContent, MAX_BOARDS_DISPLAY} from "../config/storage.js";
import {
    createElement
} from "../HTML/components/components.js";
import {
    createHTMLBoard,
    createHTMLList,
    createPaginationButtons, createRows,
    createSearchBar
} from "../HTML/DSL/components/modelComponents.js";
import {getBoardColor, getLimitSelectorOptions, getNewBoardsPath, visitBoard} from "../HTML/DSL/modelAuxs.js";
import {archivedDropdown, usersDropdown} from "../HTML/DSL/dropdowns/modelDropdowns.js";
import {cardModalHTML, listModalHTML} from "../HTML/DSL/modals/modals.js";


async function getBoards(args) {
    document.title = "OurTrello | Boards"

    createElement("h1", "My Boards")

    const res =
        await fetchReq(`board?skip=${args.skip}&limit=${args.limit}${args.name != null ? `&name=${args.name}` : ''}${args.numLists != null ? `&numLists=${args.numLists}` : ''}`,
            "GET")

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
    const select = createElement("select", null, null, null, ...options)
    select.value = args.limit
    select.addEventListener("change", (ev) =>
        document.location = getNewBoardsPath(0, ev.target.value, args.name, args.numLists)
    )

    createElement("div", null, "paginationContainer", null,
        createElement("div", "Boards per Page: ", "pagination-control", null, select),
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

    const board = await fetchReq(`board/${id}`, "GET")
    document.title = `OurTrello | ${board.name}`
    visitBoard(board)

    createElement("div", null, "board-header", null,
        createElement("h1", board.description, "board-desc"),
        createElement("div", null, "board-buttons", null,
            await usersDropdown(board.idBoard),
            await archivedDropdown(board)
        )
    )

    const listCards = board.lists.map(list => createHTMLList(list))
    const createListButton = createElement("button", "Add new List", "create-list-button")
    createListButton.addEventListener("click", () => createList(boardContainer, board))

    const boardContainer = createElement("div", null, "board", "boardContainer",
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