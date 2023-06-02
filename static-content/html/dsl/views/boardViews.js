import {div, h1} from "../../components/elements.js";
import {MAX_BOARDS_DISPLAY} from "../../../config.js";
import {updateBoardsPath} from "../modelAuxs.js";
import {coloredContainer, createRows} from "../../components/containers.js";
import {boardFunc} from "../listeners/boardFuncs.js";
import Pagination from "../components/pagination.js";


function boardsPageView(boards, totalBoards, skip, limit, name, numLists) {
    skip = parseInt(skip)
    limit = parseInt(limit)

    h1("My Boards")

    updateBoardsPath(skip, limit, name, numLists, totalBoards)

    const pagContainer = div(null, ["paginationContainer"])

    const boardsContainer = createRows([], MAX_BOARDS_DISPLAY)

    const pagination = new Pagination(boardsContainer, skip, limit, totalBoards)

    pagContainer.replaceChildren(
        pagination.createLimitSelector(),
        pagination.createPaginationButtons(boardsContainer),
        pagination.createSearchBar()
    )
}

export function renderBoards(boards, name, numLists, currBoardsContainer) {

    const boardsContainers = boards.map(board =>
        coloredContainer(board.name, board.description, board.numLists, () =>
            boardFunc(board), board.primaryColor, board.secondaryColor
        )
    )

    currBoardsContainer.replaceChildren(createRows(boardsContainers, MAX_BOARDS_DISPLAY))
}

function boardPageView() {
    // TODO
}

export default {
    boardsPageView,
    renderBoards,
    boardPageView
}