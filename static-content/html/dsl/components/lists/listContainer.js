import {button, div} from "../../../common/components/elements.js";
import {deleteList} from "../../listeners/listFuncs.js";
import {cardFunc, createCard} from "../../listeners/cardFuncs.js";
import {getNextCard} from "../../modelAuxs.js";
import cardData from "../../../../data/cardData.js";
import cardContainer from "../cards/cardContainer.js";

export default function listContainer(list) {
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
        const dragging = document.querySelector('.dragging')
        if (!listCards.contains(event.relatedTarget)) {
            const origin = document.getElementById(`list${dragging.dataset.idList}`)
            const childrenArray = [...origin.children]
            const afterOrigin = childrenArray.find(it => parseInt(it.dataset.idx) === parseInt(dragging.dataset.idx) + 1)
            if (afterOrigin) {
                origin.insertBefore(dragging, afterOrigin)
            } else {
                origin.appendChild(dragging)
            }
        }
    })

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
