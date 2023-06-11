import boardData from "../../../data/boardData.js";
import {input} from "../../common/components/elements.js";
import listData from "../../../data/listData.js";
import listContainer from "../components/lists/listContainer.js";
import userData from "../../../data/userData.js";
import {createItemDropdownUser} from "../dropdowns/modelDropdowns.js";

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

export async function addUserToBoard(boardBtnContainer, board) {

    const inputHtml = input("", ["add-user-to-board-input"], "inputEmail", "Enter email to add user")
    const addUserToBoardBtn = boardBtnContainer.querySelector('.add-user-to-board-button')
    boardBtnContainer.insertBefore(inputHtml, addUserToBoardBtn)
    inputHtml.focus()
    const handleAddUserToBoard = async () => {
        if(inputHtml.value.trim() === ""){
            boardBtnContainer.removeChild(inputHtml)
            return
        }
        await addUserToBoardOperation(boardBtnContainer, inputHtml, addUserToBoardBtn, board)
    }
    inputHtml.addEventListener("focusout", handleAddUserToBoard)
    inputHtml.addEventListener("keydown", (event) => {
        if(event.key !== "Enter" || event.repeat) return
        inputHtml.removeEventListener("focusout", handleAddUserToBoard)
        handleAddUserToBoard()
    })
}

async function addUserToBoardOperation(boardBtnContainer, input, addUserToBoardBtn, board) {
    input.remove()
    const res = await boardData.addUserToBoard(board.idBoard, input.value)

    // Add User to Dropdown User
    if(res){
        const userDropdown = boardBtnContainer.querySelector('#dropdownMenu-users')
        const liHtml = createItemDropdownUser(res)
        userDropdown.appendChild(liHtml)
    }

}
