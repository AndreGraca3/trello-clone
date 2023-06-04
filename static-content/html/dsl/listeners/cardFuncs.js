import {input, li, span} from "../../common/components/elements.js";
import cardData from "../../../data/cardData.js";
import cardContainer from "../components/cards/cardContainer.js";


const cardFunc = async (card) => {

    const fetchedCard = await cardData.getCard(card.idBoard, card.idCard)

    document.querySelector("#CardTitleModal").innerText = fetchedCard.name
    document.querySelector("#CardStartDateModal").innerText = fetchedCard.startDate

    if(fetchedCard.description !== null) {
        document.querySelector("#Description-textBox").value = fetchedCard.description
    } else document.querySelector("#Description-textBox").value = ""

    if(fetchedCard.endDate !== null) {
        document.querySelector("#endDateTime").value = fetchedCard.endDate.replace(" ", "T")
    }else document.querySelector("#endDateTime").value = ""

    document.querySelector("#cardSaveButton").onclick = async () => saveCard(fetchedCard)
    document.querySelector("#cardArchiveButton").onclick = async () => archiveCard(fetchedCard)
    document.querySelector("#cardDeleteButton").onclick = async () => deleteCard(fetchedCard)

    $('#cardModal').modal('show')
}

async function createCard(listCards, list) {
    const inputHtml = input()
    listCards.appendChild(inputHtml)
    listCards.scrollTop = listCards.scrollHeight
    inputHtml.focus()
    const handleAddCard = async () => {
        if(inputHtml.value.trim() === ""){
            listCards.removeChild(inputHtml)
            return
        }
        await addCard(listCards, inputHtml, list)
    }

    inputHtml.addEventListener("focusout", handleAddCard)
    inputHtml.addEventListener("keydown", (event) => {
        if(event.key !== "Enter" || event.repeat) return
        inputHtml.removeEventListener("focusout", handleAddCard)
        handleAddCard()
    })
}

async function addCard(listCards, input, list) {
    const card = {
        idList: list.idList,
        name: input.value,
        description: null,
        endDate: null
    }

    input.remove()
    const cardId = await cardData.createCard(list.idBoard, card.idList, card.name)

    card.idList = list.idList
    card.idBoard = list.idBoard
    card.idCard = cardId
    card.idx = listCards.lastChild? parseInt(listCards.lastChild.dataset.idx) + 1 : 1
    const cardElem = cardContainer(card)
    cardElem.addEventListener("click", () => {cardFunc(card)})
    listCards.appendChild(cardElem)
}

async function archiveCard(card) {
    const cardToMove = document.querySelector(`#Card${card.idCard}`)
    const list = document.querySelector(`#list${card.idList}`)
    const archivedContainer = document.querySelector(`#dropdownMenu-archived`)

    if(!card.archived) {
        // move to archived
        list.removeChild(cardToMove)
        moveToArchivedContainer(card, archivedContainer)
    } else {
        // return to origin
        // TODO: check if list exits if i want to unarchive
        archivedContainer.removeChild(cardToMove)
        const DeArchivedCard = cardContainer(card, async () => cardFunc(card))
        list.appendChild(DeArchivedCard)
    }

    let newEndDate = document.querySelector("#endDateTime").value.replace("T", " ")

    const newDescription = document.querySelector("#Description-textBox").value

    await cardData.updateCard(card.idBoard, card.idCard, !card.archived, newDescription, newEndDate, null)
    $('#cardModal').modal('hide')
}

async function deleteCard(card) {
    await cardData.deleteCard(card.idBoard, card.idCard)

    const cardToDelete = document.querySelector(`#Card${card.idCard}`)

    if(!card.archived) {
        const list = document.querySelector(`#list${card.idList}`)
        list.removeChild(cardToDelete)
    } else {
        const archivedContainer = document.querySelector(`#dropdownMenu-archived`)
        archivedContainer.removeChild(cardToDelete)
    }

    $('#cardModal').modal('hide')
}

async function saveCard(card) {

    const newEndDate = document.querySelector("#endDateTime").value.replace("T", " ")

    let newDescription = document.querySelector("#Description-textBox").value

    if(newDescription === "") newDescription = null

    await cardData.updateCard(card.idBoard, card.idCard, card.archived, newDescription, newEndDate, card.idList)
    $('#cardModal').modal('hide')
}

function updateIdxs(srcList, dstList, idxSrc, idxDst) {
    Array.from(srcList.childNodes).forEach(card => {
        if(card.dataset.idx >= idxSrc) card.dataset.idx--
    })

    Array.from(dstList.childNodes).forEach(card => {
        if(card.dataset.idx >= idxDst) card.dataset.idx++
    })
}

function getNextCard(container, y) {
    const draggableElements = Array.from(container.querySelectorAll('.card-container:not(.dragging)'))

    return draggableElements.reduce((closest, card) => {
        const box = card.getBoundingClientRect()
        const offset = y - box.top - box.height / 2
        if (offset < 0 && offset > closest.offset) return {offset: offset, element: card}
        else return closest
    }, {offset: Number.NEGATIVE_INFINITY}).element
}

function moveToArchivedContainer(card, archivedContainer) {
    const newArchived = li(null, ["dropdown-item", "clickable"],
        `Card${card.idCard}`,
        span("📋 " + card.name)
    )
    newArchived.addEventListener("click", async () => cardFunc(card))

    archivedContainer.appendChild(newArchived)
}

export default {
    cardFunc,
    createCard,
    updateIdxs,
    getNextCard,
    moveToArchivedContainer
}