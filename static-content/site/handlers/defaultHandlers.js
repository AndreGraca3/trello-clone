import {mainContent, MAX_RECENT_BOARDS} from "../../config.js";
import {div, h1} from "../../html/common/components/elements.js";
import {boardFunc} from "../../html/dsl/listeners/boardFuncs.js";
import {coloredContainer, createRows} from "../../html/common/components/containers.js";
import {RECENT_BOARDS} from "../../data/boardData.js";


function getHome() {

    h1("Welcome to OurTrello!", ["rainbow-text"])
    h1("🕒 Recent Boards", ["recent-boards"])

    const recentBoards = RECENT_BOARDS.map(board => {
        return coloredContainer(board.name, "", null,
            () => boardFunc(board), board.primaryColor, board.secondaryColor, 5
        )
    })

    const recent = createRows(recentBoards, MAX_RECENT_BOARDS)
    recent.classList.add("recent-rows")
    div(null, [], null, recent)
}

function getErrorPage(error) {
    document.title = "OurTrello | Error"

    mainContent.replaceChildren()

    h1("NOT THE BOARD YOU'RE LOOKING FOR", ["error-header"])
    h1(error)

    div(null, ["bg-error"])
}

export default {
    getHome,
    getErrorPage
}