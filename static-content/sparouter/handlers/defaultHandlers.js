import {createHTMLBoard} from "../utils/auxs/utils.js"
import {MAX_RECENT_BOARDS, RECENT_BOARDS} from "../utils/storage.js";
import {createElement, createRows} from "../utils/components/components.js";
import {boardFunc} from "../utils/listenerHandlers/boardFuncs.js";
import {getBoardColor} from "../utils/auxs/modelAuxs.js";


function getHome() {
    document.title = "OurTrello | Home"

    createElement("h1", "Welcome to OurTrello!", "rainbow-text")
    createElement("h1", "🕒 Recent Boards", "recent-boards")

    const recentBoards = RECENT_BOARDS.map(board => {
        return createHTMLBoard(board.name, "", "", () => boardFunc(board), getBoardColor(board.idBoard), 5)
    })

    const recent = createRows(recentBoards, MAX_RECENT_BOARDS)
    recent.classList.add("recent-rows")
    createElement("div", null, null, null, recent)
}

function getErrorPage(error) {
    document.title = "OurTrello | Error"

    createElement("h1", "NOT THE BOARD YOU'RE LOOKING FOR", "error-header")
    createElement("h1", error)

    createElement("div", null, "bg-error")
}

export default {
    getHome,
    getErrorPage
}