import {
    createHeader,
    createHTMLBoardBox,
    createRows,
    getBoardColor
}
    from "./utils/utils.js"

import {
    boardFunc
}
    from "./utils/buttonFuncs.js";

import {
    MAX_RECENT_BOARDS,
    RECENT_BOARDS
}
    from "./utils/storage.js";

function getHome(mainContent) {
    document.title = "OurTrello | Home"

    const h1 = createHeader("Welcome to OurTrello!")
    h1.classList.add("rainbow-text")

    const h2 = createHeader("🕒 Recent Boards")
    h2.classList.add("recent-boards")

    const recentBoards = RECENT_BOARDS.map(board => {
        return createHTMLBoardBox(board.name, "", "", () => boardFunc(board), getBoardColor(board.idBoard), 5)
    })

    const recent = createRows(recentBoards, MAX_RECENT_BOARDS)
    recent.classList.add("recent-rows")

    mainContent.replaceChildren(h1, h2, recent)
}

function getErrorPage(mainContent, error) {
    document.title = "OurTrello | Error"

    const h1 = createHeader("NOT THE BOARD YOU'RE LOOKING FOR")
    h1.classList.add("error-header")

    const h2 = createHeader(error)

    const bg = document.createElement("div")
    bg.classList.add("bg-error")

    mainContent.replaceChildren(h1, h2, bg)
}

export const defaultHandler = {
    getHome,
    getErrorPage
}

export default defaultHandler