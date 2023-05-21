import {fetchReq} from "../auxs/utils.js";
import {createHTMLList} from "../components/modelComponents.js";

export async function createList(boardContainer, board) {
    const input = document.createElement("input")
    boardContainer.insertBefore(input, boardContainer.querySelector('.create-list-button'))
    boardContainer.scrollLeft = boardContainer.scrollWidth
    input.focus()
    const handleAddList = async () => {
        if(input.value.trim() === ""){
            boardContainer.removeChild(input)
            return
        }
        await addList(boardContainer, input, board)
    }
    input.addEventListener("focusout", handleAddList)
    input.addEventListener("keydown", (event) => {
        if(event.key !== "Enter" || event.repeat) return
        input.removeEventListener("focusout", handleAddList)
        handleAddList()
    })
}

async function addList(boardContainer, input, board) {
    const list = {
        name: input.value
    }
    const idList = await fetchReq(`board/${board.idBoard}/list`, "POST", list)
    list.idBoard = board.idBoard
    list.idList = idList
    boardContainer.insertBefore(createHTMLList(list), input)
    input.remove()
}

export async function deleteList(list) {
    const listToDelete = document.querySelector(`#List${list.idList}`)

    const card = listToDelete.querySelector(".card-container")

    if(!card) {
        await fetchReq(`board/${list.idBoard}/list/${list.idList}`, "DELETE")

        const board = document.querySelector("#boardContainer")
        board.removeChild(listToDelete)
    } else {
        $('#listModal').modal('show')
    }
}
