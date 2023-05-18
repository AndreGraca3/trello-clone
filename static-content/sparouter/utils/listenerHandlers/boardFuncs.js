import {fetchReq} from "../auxs/utils.js";

export const boardFunc = (board) => {
    document.location = `#board/${board.idBoard}`
}

export async function createBoard() {
    const boardName = $('#board-name').val()
    const boardDesc = $('#board-description').val()

    try {
        const res = await fetchReq("board", "POST", {name: boardName, description: boardDesc})

        document.querySelector('.toast-body').innerText = "Board Created Successfully!"
        $('.toast').toast('show')

        setTimeout(() => {
            hideCreateBoardButton()
            document.location = `#board/${res.idBoard}`
        }, 2000)
    } catch (e) {
        document.querySelector('.toast-body').innerText = e
        $('.toast').toast('show')
    }
}

export function showCreateBoardButton() {
    $('#createBoardModal').modal('show')
}

export function hideCreateBoardButton() {
    $('#createBoardModal').modal('hide')
}