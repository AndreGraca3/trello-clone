import {fetchReq} from "../auxs/utils.js";
import {createHTMLList} from "../components/modelComponents.js";
import {moveToArchivedContainer} from "../auxs/modelAuxs.js";

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

    const deleteHandler = () => {
        const board = document.querySelector("#boardContainer")
        board.removeChild(listToDelete)
        $('#listModal').modal('hide')
    }

    if(!card) {
        await fetchReq(`board/${list.idBoard}/list/${list.idList}`, "DELETE")
        deleteHandler()
    } else {
        $('#listModal').modal('show')

        document.querySelector('#listDeleteButton').onclick = async () => {
            await fetchReq(`board/${list.idBoard}/list/${list.idList}?action=delete`, "DELETE")
            deleteHandler()
        }

        document.querySelector('#listArchiveButton').onclick = async () => {
            await fetchReq(`board/${list.idBoard}/list/${list.idList}?action=archive`, "DELETE")
            // move them visually
            const archivedContainer = document.querySelector(`#dropdownMenu-archived`)
            const listContainer = document.querySelector(`#list${list.idList}`)
            listContainer.childNodes.forEach((c) => {
                const archCard = {
                    idBoard: list.idBoard,
                    idCard: c.dataset.idCard,
                    name: c.innerText,
                }
                moveToArchivedContainer(archCard, archivedContainer)
            })
            deleteHandler()
        }
    }
}