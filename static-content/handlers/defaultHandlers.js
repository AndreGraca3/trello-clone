import {MAX_RECENT_BOARDS, RECENT_BOARDS} from "../config/storage.js";
import {div, h1} from "../HTML/components/components.js";
import {boardFunc} from "../HTML/DSL/listeners/boardFuncs.js";
import {getBoardColor} from "../HTML/DSL/modelAuxs.js";
import {createHTMLBoard, createRows} from "../HTML/DSL/components/modelComponents.js";

function getHome() {
    document.title = "OurTrello | Home"

    h1("Welcome to OurTrello!", ["rainbow-text"])
    h1("🕒 Recent Boards", ["recent-boards"])

    const recentBoards = RECENT_BOARDS.map(board => {
        return createHTMLBoard(board.name, "", null, () => boardFunc(board), getBoardColor(board.idBoard), 5)
    })

    const recent = createRows(recentBoards, MAX_RECENT_BOARDS)
    recent.classList.add("recent-rows")
    div(null, [], null, recent)
}

function getErrorPage(error) {
    document.title = "OurTrello | Error"

    h1("NOT THE BOARD YOU'RE LOOKING FOR", ["error-header"])
    h1(error)

    div(null, ["bg-error"])
}

export default {
    getHome,
    getErrorPage
}