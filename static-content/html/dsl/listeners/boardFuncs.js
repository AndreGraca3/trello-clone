import boardData from "../../../data/boardData.js";

export const boardFunc = (board) => {
    document.location = `#board/${board.idBoard}`
}

export async function createBoard() {
    const boardName = $('#board-name').val()
    const boardDesc = $('#board-description').val()

    const res = await boardData.createBoard(boardName, boardDesc)

    document.querySelector('.toast-body').innerText = "Board Created Successfully!"
    $('.toast').toast('show')

    hideCreateBoardModal()
    document.location = `#board/${res.idBoard}`
}

export function showCreateBoardModal() {
    $('#createBoardModal').modal('show')
}

export function hideCreateBoardModal() {
    $('#board-name').val("")
    $('#board-description').val("")
    $('#createBoardModal').modal('hide')
}

// Changes browser URL. This doesn't fire the hashchange effect
export function updateBoardsPath(skip, limit, nameSearch, numLists, totalBoards) {
    if (skip < 0) skip = 0
    if (skip > totalBoards) skip = Math.max(totalBoards - limit + 1, 0)

    const newPath = `#boards?skip=${skip}&limit=${limit}${nameSearch != null && nameSearch !== "" ? `&name=${nameSearch}` : ''}${numLists != null && numLists !== "" ? `&numLists=${numLists}` : ''}`
    window.history.pushState(null, "", newPath)
    return {skip, limit} // newPath,
}