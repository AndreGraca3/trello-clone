import {button, input, li, span} from "../../common/components/elements.js";
import cardData from "../../../data/cardData.js";
import cardContainer from "../components/cards/cardContainer.js";
import {listDropdown} from "../dropdowns/modelDropdowns.js";


const cardFunc = async (card) => {

    const fetchedCard = await cardData.getCard(card.idBoard, card.idCard)

    document.querySelector("#CardTitleModal").innerText = fetchedCard.name
    document.querySelector("#CardStartDateModal").innerText = fetchedCard.startDate

    const descriptionBox = document.querySelector("#Description-textBox")
    console.log(descriptionBox)
    if (fetchedCard.description !== null) {
        descriptionBox.value = fetchedCard.description
    } else descriptionBox.value = ""

    const endDateTime = document.querySelector("#endDateTime")
    if (fetchedCard.endDate !== null) {
        endDateTime.value = fetchedCard.endDate.replace(" ", "T")
    } else endDateTime.value = ""

    document.querySelector("#cardSaveButton").onclick = async () => saveCard(fetchedCard)

    const archiveButton = document.querySelector("#cardArchiveButton")
    if (fetchedCard.idList == null) {
        const lists = document.querySelectorAll(".list-container")
        if (archiveButton) archiveButton.replaceWith(listDropdown(card, lists))
    } else {
        archiveButton.replaceWith(button("Archive", ["btn-primary", "btn"], "cardArchiveButton"))
        document.querySelector("#cardArchiveButton").onclick = async () => archiveCard(fetchedCard)
    }

    document.querySelector("#cardDeleteButton").onclick = async () => deleteCard(fetchedCard)

    $('#cardModal').modal('show')
}

async function createCard(listCards, list) {
    const inputHtml = input()
    listCards.appendChild(inputHtml)
    listCards.scrollTop = listCards.scrollHeight
    inputHtml.focus()
    const handleAddCard = async () => {
        if (inputHtml.value.trim() === "") {
            listCards.removeChild(inputHtml)
            return
        }
        await addCard(listCards, inputHtml, list)
    }

    inputHtml.addEventListener("focusout", handleAddCard)
    inputHtml.addEventListener("keydown", (event) => {
        if (event.key !== "Enter" || event.repeat) return
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
    card.idx = listCards.lastChild ? parseInt(listCards.lastChild.dataset.idx) + 1 : 1
    const cardElem = cardContainer(card)
    cardElem.addEventListener("click", () => {
        cardFunc(card)
    })
    listCards.appendChild(cardElem)
}

async function archiveCard(card) {
    console.log("click")

    const cardToArchive = document.querySelector(`#Card${card.idCard}`)
    const list = document.querySelector(`#list${card.idList}`)
    const archivedContainer = document.querySelector(`#dropdownMenu-archived`)

    if (!card.archived) {
        // move to archived
        list.removeChild(cardToArchive)
        moveToArchivedContainer(card, archivedContainer)
        updateIdxs(list, null, card.idx)
    }

    let newEndDate = document.querySelector("#endDateTime").value.replace("T", " ")

    const newDescription = document.querySelector("#Description-textBox").value

    await cardData.updateCard(card.idBoard, card.idCard, !card.archived, newDescription, newEndDate, null)
    $('#cardModal').modal('hide')
}

async function deArchiveCard(card, idList) {
    const cardToArchive = document.querySelector(`#Card${card.idCard}`)
    const archivedContainer = document.querySelector(`#dropdownMenu-archived`)
    let listDst = null
    document.querySelectorAll(".list-cards").forEach(list => {
            if (list.dataset.idList === idList) listDst = list
        }
    )

    console.log(listDst)

    let newEndDate = document.querySelector("#endDateTime").value.replace("T", " ")

    const newDescription = document.querySelector("#Description-textBox").value

    await cardData.updateCard(card.idBoard, card.idCard, !card.archived, newDescription, newEndDate, idList.replace("List", ""))

    archivedContainer.removeChild(cardToArchive)
    const deArchivedCard = cardContainer(card, async () => cardFunc(card))
    deArchivedCard.dataset.idList = idList
    deArchivedCard.dataset.idx = getLastIdx(listDst)
    listDst.appendChild(deArchivedCard)
    $('#cardModal').modal('hide')
}

async function deleteCard(card) {
    await cardData.deleteCard(card.idBoard, card.idCard)

    const cardToDelete = document.querySelector(`#Card${card.idCard}`)

    if (!card.archived) {
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

    if (newDescription === "") newDescription = null

    await cardData.updateCard(card.idBoard, card.idCard, card.archived, newDescription, newEndDate, card.idList)
    $('#cardModal').modal('hide')
}

function updateIdxs(srcList, dstList, idxSrc, idxDst) {
    Array.from(srcList.childNodes).forEach(card => {
        if (card.dataset.idx >= idxSrc) card.dataset.idx--
    })

    if (dstList) {
        Array.from(dstList.childNodes).forEach(card => {
            if (card.dataset.idx >= idxDst) card.dataset.idx++
        })
    }
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

function getLastIdx(list) {
    const lastCard = list.lastChild
    return lastCard.dataset.idx + 1
}

export default {
    cardFunc,
    createCard,
    updateIdxs,
    getNextCard,
    deArchiveCard,
    moveToArchivedContainer
}