import {createHTMLCard, createHTMLList, fetchReq} from "./utils.js";
import {user} from "./storage";

export const boardFunc = (board) => {
    document.location = `#board/${board.idBoard}`
}

export const cardFunc = async (card) => {

    const fetchedCard = await fetchReq(`board/${card.idBoard}/card/${card.idCard}`,"GET")

    document.querySelector("#CardTitleModal").innerText = fetchedCard.name
    document.querySelector("#CardStartDateModal").innerText = fetchedCard.startDate

    if(fetchedCard.description !== null) {
        document.querySelector("#CardDescModal").innerText = fetchedCard.description
    }
    if(fetchedCard.endDate !== null) {
        document.querySelector("#endDateTime").value = fetchedCard.endDate.replace(" ", "T")
    }

    document.querySelector("#cardSaveButton").onclick = async () => saveCard(fetchedCard)
    document.querySelector("#cardArchiveButton").onclick = async () => archiveCard(fetchedCard)
    document.querySelector("#cardDeleteButton").onclick = async () => deleteCard(fetchedCard)

    $('#cardModal').modal('show')
}

export async function createBoard() {
    const boardName = $('#board-name').val()
    const boardDesc = $('#board-description').val()

    try {
        const res = await fetchReq("board", "POST", {name: boardName, description: boardDesc})
        hideCreateBoardButton()

        document.querySelector('#pop-up').innerText = "Board created Successfully!!"
        $("#pop-up").fadeIn();
        setInterval(function () {
            $("#pop-up").fadeOut();
        },3000)

        document.location = `#board/${res.idBoard}`
    } catch (e) {
        document.querySelector('#pop-up').innerText = e
        $('#pop-up').fadeIn();
        setInterval(function () {
            $('#pop-up').fadeOut();
        },3000)
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
        idList: list.idList,
        name: input.value,
        description: null,
        endDate: null
    }
    const cardId = await fetchReq(`board/${list.idBoard}/card`, "POST", card)
    input.remove()

    card.idList = list.idList
    card.idBoard = list.idBoard
    card.idCard = cardId
    const cardElem = createHTMLCard(card)
    cardElem.addEventListener("click", () => {cardFunc(card)})
    listCards.appendChild(cardElem)
}

export function showCreateBoardButton() {
    $('#createBoardModal').modal('show')
}

export function hideCreateBoardButton() {
    $('#createBoardModal').modal('hide')
}

export async function changeUserAvatar() {
    const input = document.createElement('input');
    input.type = 'file';
    input.accept = 'image/*';

    input.addEventListener('change', () => {
        const file = input.files[0]
        if (!file) return

        const reader = new FileReader()
        reader.onload = () => {
            const imgUrl = reader.result
            fetchReq("user/avatar", "PUT", {imgUrl})
            user.avatar = imgUrl
            console.log(user)
            document.location.reload()
            console.log("reload")
        }
        reader.readAsDataURL(file)
    })

    input.click()
}

async function archiveCard(card) {
    const cardToArchive = document.querySelector(`#Card${card.idCard}`)

    const archivedCard = document.querySelector(`#ArchivedCard${card.idCard}`)

    const list = document.querySelector(`#list${card.idList}`)
    const markDown = document.querySelector(`#dropdownMenu-archived`)

    if(!card.archived) {
        list.removeChild(cardToArchive)

        const getCardArchived = document.createElement("a")
        getCardArchived.classList.add("dropdown-item")
        getCardArchived.innerText = card.name
        getCardArchived.addEventListener("click", async () => cardFunc(card))

        $("#dropdownMenu-archived").append(getCardArchived)
    } else {
        // tenho de converter no elemento de html certo.
        markDown.removeChild(archivedCard)
        const DeArchivedCard = createHTMLCard(card, async () => cardFunc(card))
        list.appendChild(DeArchivedCard)
    }

    let newEndDate = document.querySelector("#endDateTime").value.replace("T", " ")

    const newDescription = document.querySelector("#Description-textBox").value

    const Changes = {
            archived: !card.archived,
            description: newDescription,
            endDate: newEndDate
    }

    $('#cardModal').modal('hide')

    await fetchReq(`board/${card.idBoard}/card/${card.idCard}/update`, "PUT", Changes)
}
async function deleteCard(card) {
    const cardToDelete = document.querySelector(`#Card${card.idCard}`)

    const list = document.querySelector(`#list${card.idList}`)

    list.removeChild(cardToDelete)

    $('#cardModal').modal('hide')

    await fetchReq(`board/${card.idBoard}/card/${card.idCard}`, "DELETE")
}

export async function deleteList(list) {
    const listToDelete = document.querySelector(`#List${list.idList}`)

    const card = listToDelete.querySelector(".card-container")

    if(!card) {
        const board = document.querySelector("#boardContainer")

        board.removeChild(listToDelete)

        await fetchReq(`board/${list.idBoard}/list/${list.idList}`, "DELETE")
    } else {
        $('#listModal').modal('show')

        //document.querySelector("#listArchiveButton").addEventListener("click", async () => archivarOsCardsEApagarLista())
        //document.querySelector("#listDeleteButton").addEventListener("click", async () => apagarCardsELista())

    }
}

async function saveCard(card) {
    const newEndDate = document.querySelector("#endDateTime").value.replace("T", " ")

    const newDescription = document.querySelector("#Description-textBox").value

    const Changes = {
        archived: card.archived,
        description: newDescription,
        endDate: newEndDate
    }

    console.log(card)

    $('#cardModal').modal('hide')

    await fetchReq(`board/${card.idBoard}/card/${card.idCard}/update`, "PUT", Changes)
}