import {createHTMLList} from "../components/modelComponents.js";
import {fetchReq} from "../../../utils/utils.js";
import {moveToArchivedContainer} from "../modelAuxs.js";
import {input} from "../../components/components.js";
import listData from "../../../data/listData.js";
import cardData from "../../../data/cardData.js";


export async function createList(boardContainer, board) {
    const inputHtml = input()
    const createListButton = boardContainer.querySelector('.create-list-button')
    boardContainer.insertBefore(inputHtml, createListButton)
    boardContainer.scrollLeft = boardContainer.scrollWidth
    inputHtml.focus()
    const handleAddList = async () => {
        if(inputHtml.value.trim() === ""){
            boardContainer.removeChild(inputHtml)
            return
        }
        await addList(boardContainer, inputHtml, board, createListButton)
    }
    inputHtml.addEventListener("focusout", handleAddList)
    inputHtml.addEventListener("keydown", (event) => {
        if(event.key !== "Enter" || event.repeat) return
        inputHtml.removeEventListener("focusout", handleAddList)
        handleAddList()
    })
}

async function addList(boardContainer, input, board, createListButton) {
    const list = {
        name: input.value
    }
    input.remove()
    const idList = await listData.createList(board.idBoard, input.value)
    list.idBoard = board.idBoard
    list.idList = idList
    boardContainer.insertBefore(createHTMLList(list), createListButton)
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
        await cardData.deleteCard(list.idBoard, list.idList)
        deleteHandler()
    } else {
        $('#listModal').modal('show')

        document.querySelector('#listDeleteButton').onclick = async () => {
            await listData.deleteList(list.idBoard, list.idList, "delete")
            deleteHandler()
        }

        document.querySelector('#listArchiveButton').onclick = async () => {
            await listData.deleteList(list.idBoard, list.idList, "archive")
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