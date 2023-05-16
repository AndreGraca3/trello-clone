import {
    archivedDropdown, cardModalHTML,
    createHeader,
    createHTMLBoardBox, createHTMLList,
    createRows, darkerColor,
    fetchReq,
    getBoardColor, listModalHTML, usersDropdown,
    visitBoard
}
    from "./utils/utils.js";

import {
    LIMIT_INITIAL_VALUE,
    MAX_BOARDS_DISPLAY
}
    from "./utils/storage.js";

import {
    boardFunc,
    createList
}
    from "./utils/buttonFuncs.js";

async function getBoards(mainContent, args) {

    document.title = "OurTrello | Boards"

    console.log(args)
    const h1 = createHeader("My Boards")
    mainContent.replaceChildren(h1)

    if (args.skip === undefined) {
        args.skip = 0
    }
    if (args.limit === undefined) {
        args.limit = LIMIT_INITIAL_VALUE
    }

    const boards = await fetchReq(`board?skip=${args.skip}&limit=${args.limit}`, "GET")
    document.location = `#boards?skip=${args.skip}&limit=${args.limit}`

    const cards = boards.map(board => {
        console.log(board)
        return createHTMLBoardBox(board.name, board.description, board.numLists, () => boardFunc(board), getBoardColor(board.idBoard))
    })

    const boardsContainer = createRows(cards, MAX_BOARDS_DISPLAY)

    /** Pagination Buttons**/
    const paginationContainer = document.createElement("div")
    paginationContainer.classList.add("pagination")

    const prevBtn = document.createElement("button")
    prevBtn.classList.add("btn", "btn-secondary")
    prevBtn.textContent = "Previous"
    prevBtn.disabled = args.skip === 0
    prevBtn.addEventListener("click", () => {
        args.skip -= args.limit
        getBoards(mainContent, args)
    })

    const nextBtn = document.createElement("button")
    nextBtn.classList.add("btn", "btn-secondary")
    nextBtn.textContent = "Next"
    nextBtn.disabled = cards.length < MAX_BOARDS_DISPLAY
    nextBtn.addEventListener("click", () => {
        args.skip += args.limit
        getBoards(mainContent, args)
    })

    // Add margin to the buttons
    prevBtn.style.marginRight = "5px"
    nextBtn.style.marginLeft = "5px"

    // Center the buttons
    paginationContainer.style.display = "flex"
    paginationContainer.style.justifyContent = "center"

    paginationContainer.appendChild(prevBtn)
    paginationContainer.appendChild(nextBtn)
    mainContent.appendChild(paginationContainer)
    mainContent.appendChild(boardsContainer)
}


async function getBoard(mainContent, args) {
    const id = args.idBoard

    const board = await fetchReq(`board/${id}`, "GET")
    document.title = `OurTrello | ${board.name}`
    visitBoard(board)

    const boardHeader = document.createElement("h5")

    const desc = document.createElement("p")
    desc.innerText = board.description.charAt(0).toUpperCase() + board.description.slice(1)
    desc.classList.add("board-desc")

    const archived = await archivedDropdown(board)

    const users = await usersDropdown(board.idBoard)

    boardHeader.replaceChildren(desc, users, archived)

    const boardContainer = document.createElement("div")
    boardContainer.classList.add("board")
    boardContainer.id = "boardContainer"


    board.lists.forEach(list => {
        const listContainer = createHTMLList(list)
        boardContainer.appendChild(listContainer)
    })

    const createListButton = document.createElement("button")
    createListButton.innerText = "Add new List"
    createListButton.classList.add("create-list-button")

    createListButton.addEventListener("click", () => createList(boardContainer, board))

    boardContainer.appendChild(createListButton)

    const color = getBoardColor(board.idBoard)
    mainContent.style.background = `linear-gradient(135deg, ${darkerColor(color)}, ${color})`

    const cardModal = cardModalHTML()
    const listModal = listModalHTML()

    mainContent.replaceChildren(boardHeader, boardContainer, cardModal, listModal)
}

export const boardHandler = {
    getBoards,
    getBoard
}

export default boardHandler