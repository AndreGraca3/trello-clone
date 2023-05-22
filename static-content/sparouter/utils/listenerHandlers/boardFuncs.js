import {fetchReq} from "../auxs/utils.js";

export const boardFunc = (board) => {
    document.location = `#board/${board.idBoard}`
}

export async function createBoard() {
    const boardName = $('#board-name').val()
    const boardDesc = $('#board-description').val()

    const res = await fetchReq("board", "POST", {name: boardName, description: boardDesc})

    document.querySelector('.toast-body').innerText = "Board Created Successfully!"
    $('.toast').toast('show')

    hideCreateBoardModal()
    document.location = `#board/${res.idBoard}`
}

export function showCreateBoardModal() {
    $('#createBoardModal').modal('show')
}

export function hideCreateBoardModal() {
    $('#createBoardModal').modal('hide')
}