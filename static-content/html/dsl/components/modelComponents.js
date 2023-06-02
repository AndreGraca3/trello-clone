import {a, button, div, input, li, nav, option, select, ul} from "../../components/elements.js";
import {deleteList} from "../listeners/listFuncs.js";
import {cardFunc, createCard} from "../listeners/cardFuncs.js";
import {addOrChangeQuery, updateBoardsPath, getNextCard} from "../modelAuxs.js";
import cardData from "../../../data/cardData.js";


export function listContainer(list) {
    const listHeader = div(list.name, ["list-header"])

    const deleteButton = button("🗑️", ["listDeleteButton", "btn"])
    deleteButton.addEventListener("click", async () => deleteList(list))

    const listCards = div(null, ["list-cards"], `list${list.idList}`)

    if (list.cards) {
        list.cards.forEach((card) => {
            if (!card.archived) {
                const cardElement = cardContainer(card, () => cardFunc(card));
                listCards.appendChild(cardElement);
            }
        })
    }

    listCards.addEventListener("dragover", (event) => {
        event.preventDefault()
        const dragging = document.querySelector('.dragging')
        const afterCard = getNextCard(listCards, event.clientY)
        if (afterCard != null) listCards.insertBefore(dragging, afterCard)
        else listCards.appendChild(dragging)
    })

    listCards.addEventListener("dragleave", (event) => {
        const dragging = document.querySelector('.dragging') // falta saber o idx
        if (!listCards.contains(event.relatedTarget)) {
            const origin = document.getElementById(`list${dragging.dataset.idList}`) // search do idx anterior e colocá-lo à frente (ter em conta que pode estar vazia)
            // TODO: back to original idx
            origin.appendChild(dragging)
        }
    });

    listCards.addEventListener("drop", async (event) => {
        const card = document.querySelector('.dragging')
        const nextCard = getNextCard(listCards, event.clientY)
        let idList = listCards.id.split("list")[1]
        let cix
        if (nextCard != null) {
            cix = Array.from(listCards.childNodes).indexOf(nextCard)
        } else {
            cix = listCards.childNodes.length
        }
        console.log(`Moved Card ${card.dataset.idCard} from list ${card.dataset.idList} to list ${idList} and cix ${cix}`)

        await cardData.moveCard(list.idBoard, card.dataset.idCard, card.dataset.idList, idList, cix)

        card.dataset.idList = idList
        card.dataset.idx = cix
    })

    const newCardButton = button("Add Card", ["newCard"])
    newCardButton.addEventListener("click", async () => {
        await createCard(listCards, list)
    })

    return div(null, ["list-container"], `List${list.idList}`,
        listHeader, deleteButton, listCards, newCardButton
    )
}

export function cardContainer(card, clickableFunc) {

    const cardContainer = button(null, ["card-container"], `Card${card.idCard}`,
        div(card.name, ["card-content"])
    )

    cardContainer.draggable = true;
    cardContainer.addEventListener("dragstart", () => {
        cardContainer.classList.add("dragging")
    })
    cardContainer.addEventListener("dragend", () => {
        cardContainer.classList.remove("dragging")
    })
    cardContainer.dataset.idCard = card.idCard
    cardContainer.dataset.idList = card.idList
    cardContainer.dataset.idx = card.idx

    if (clickableFunc) cardContainer.addEventListener("click", clickableFunc)

    cardContainer.classList.add("list-group-item", "list-group-item-action")

    return cardContainer
}