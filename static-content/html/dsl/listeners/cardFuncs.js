import {fetchReq} from "../../../utils.js";
import {createElement, input} from "../../components/elements.js";
import {cardContainer} from "../components/modelComponents.js";
import {moveToArchivedContainer} from "../modelAuxs.js";
import cardData from "../../../data/cardData.js";


export const cardFunc = async (card) => {

    const fetchedCard = await cardData.getCard(card.idBoard, card.idCard)

    console.log(fetchedCard)

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

export async function createCard(listCards, list) {
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
        // return to origin TODO: check if list exits if i want to unarchive
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

    console.log(card)
    const newEndDate = document.querySelector("#endDateTime").value.replace("T", " ")

    let newDescription = document.querySelector("#Description-textBox").value

    if(newDescription === "") newDescription = null

    await cardData.updateCard(card.idBoard, card.idCard, card.archived, newDescription, newEndDate, card.idList)
    $('#cardModal').modal('hide')
}
