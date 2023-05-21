import {darkerColor, fetchReq} from "../utils/auxs/utils.js";
import {LIMIT_INITIAL_VALUE, mainContent, MAX_BOARDS_DISPLAY, PAGINATION_CONTROL_VALUES} from "../utils/storage.js";
import {createElement, createRows, createSearchBar} from "../utils/components/components.js";
import {createHTMLBoard, createHTMLList, createPaginationButtons} from "../utils/components/modelComponents.js";
import {archivedDropdown, usersDropdown} from "../utils/dropdowns/modelDropdowns.js";
import {boardFunc} from "../utils/listenerHandlers/boardFuncs.js";
import {getBoardColor, visitBoard} from "../utils/auxs/modelAuxs.js";
import {cardModalHTML, listModalHTML} from "../utils/modals/modals.js";
import {createList} from "../utils/listenerHandlers/listFuncs.js";


async function getBoards(args) {
    document.title = "OurTrello | Boards"

    createElement("h1", "My Boards")

    const res =
        await fetchReq(`board?skip=${args.skip}&limit=${args.limit}${args.name!=null ? `&name=${args.name}` : ''}${args.numLists != null ? `&numLists=${args.numLists}` : ''}`,
            "GET")

    const boards = res.boards

    // Prevent from going off range
    if (!args.skip) args.skip = 0
    if (!args.limit) args.limit = LIMIT_INITIAL_VALUE
    if (args.skip < 0) args.skip = Math.max(0, args.skip)
    if (args.skip > res.totalBoards) args.skip = Math.min(res.totalBoards - args.limit + 1, args.skip)
    document.location =
        `#boards?skip=${args.skip}&limit=${args.limit}${args.name!=null ? `&name=${args.name}` : ''}${args.numLists!= null? `&numLists=${args.numLists}` : '' }`

    const boardCards = boards.map(board =>
        createHTMLBoard(board.name, board.description, board.numLists, () =>
            boardFunc(board), getBoardColor(board.idBoard))
    )

    // Pagination limit selector
    if (!PAGINATION_CONTROL_VALUES.includes(args.limit)) {
        PAGINATION_CONTROL_VALUES.push(args.limit)
        PAGINATION_CONTROL_VALUES.sort((a, b) => a - b)
    }

    const options = PAGINATION_CONTROL_VALUES.map(it => createElement("option", it))
    const select = createElement("select", null, null, null, ...options)
    select.value = args.limit
    select.addEventListener("change", (ev) => document.location = `#boards?skip=${args.skip}&limit=${ev.target.value}`)

    const searchBar = createSearchBar()

    createElement("div", null, "paginationContainer", null,
        createElement("div", "Boards per Page: ", "pagination-control", null, select),
        createPaginationButtons(args.skip, args.limit, res.totalBoards, args.name),
        searchBar
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