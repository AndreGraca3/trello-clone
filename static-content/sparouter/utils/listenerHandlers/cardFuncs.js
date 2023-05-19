import {fetchReq} from "../auxs/utils.js";
import {createHTMLCard} from "../components/modelComponents.js";

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
    const cardId = await fetchReq(`board/${list.idBoard}/card`, "POST", card)   // TODO: Has to return idx
    input.remove()

    card.idList = list.idList
    card.idBoard = list.idBoard
    card.idCard = cardId
    const cardElem = createHTMLCard(card)
    cardElem.addEventListener("click", () => {cardFunc(card)})
    listCards.appendChild(cardElem)
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
