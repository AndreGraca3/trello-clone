import {createHTMLCard, createHTMLList, fetchReq} from "./utils.js";

export const boardFunc = (board) => {
    document.location = `#board/${board.idBoard}`
}

export const cardFunc = (card) => {
    document.location = `#board/${card.idBoard}/list/${card.idList}/card/${card.idCard}`
}

export async function createBoard() {
    const boardName = $('#board-name').val()
    const boardDesc = $('#board-description').val()

    try {
        const res = await fetchReq("board", "POST", {name: boardName, description: boardDesc})
        hideCreateBoardButton()
        alert('Board Created Successfully')
        document.location = `#board/${res.idBoard}`
    } catch (e) {
        alert(e)
    }
}

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
    input.addEventListener("keydown", async (event) => {
        if(event.key !== "Enter") return
        input.removeEventListener("focusout", handleAddList)
        await handleAddList()
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

export async function createCard(listCards, list) {
    const input = document.createElement("input")
    listCards.appendChild(input)
    listCards.scrollTop = listCards.scrollHeight
    input.focus()
    const handleAddCard = async () => {
        if(input.value.trim() === ""){
            listCards.removeChild(input)
            return
        }
        await addCard(listCards, input, list)
    }

    input.addEventListener("focusout", handleAddCard)
    input.addEventListener("keydown", async (event) => {
        if(event.key !== "Enter") return
        input.removeEventListener("focusout", handleAddCard)
        await handleAddCard()
    })
}

async function addCard(listCards, input, list) {
    const card = {
        name: input.value,
        description: null,
        endDate: null
    }
    const cardId = await fetchReq(`board/${list.idBoard}/list/${list.idList}/card`, "POST", card)
    input.remove()

    card.idList = list.idList
    card.idBoard = list.idBoard
    card.idCard = cardId
    listCards.appendChild(createHTMLCard(card, () => {
        cardFunc(card)
    }))
}

export function showCreateBoardButton() {
    $('#createBoardModal').modal('show')
}

export function hideCreateBoardButton() {
    $('#createBoardModal').modal('hide')
}