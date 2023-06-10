import {button, div} from "../../../common/components/elements.js";
import {deleteList} from "../../listeners/listFuncs.js";
import cardContainer from "../cards/cardContainer.js";
import draggingFuncs from "../../listeners/draggingFuncs.js";
import cardFuncs from "../../listeners/cardFuncs.js";


export default function listContainer(list) {
    const listHeader = div(list.name, ["list-header"])

    const deleteButton = button("🗑️", ["listDeleteButton", "btn"])
    deleteButton.addEventListener("click", async () => deleteList(list))

    const listCards = div(null, ["list-cards"], `list${list.idList}`)

    if (list.cards) {
        list.cards.forEach((card) => {
            if (!card.archived) {
                const cardElement = cardContainer(card, () => cardFuncs.cardFunc(card));
                listCards.appendChild(cardElement);
            }
        })
    }

    listCards.addEventListener("dragover", (event) => draggingFuncs.handleDrag(event, listCards))

    listCards.addEventListener("dragleave", (event) => draggingFuncs.handleDragLeave(event, listCards))

    listCards.addEventListener("drop", async (event) => draggingFuncs.handleDragDrop(event, listCards, list))

    const newCardButton = button("Add Card", ["newCard"])
    newCardButton.addEventListener("click", async () => {
        await cardFuncs.createCard(listCards, list)
    })

    return div(null, ["list-container"], `List${list.idList}`,
        listHeader, deleteButton, listCards, newCardButton
    ) // `List${list.idList}` => null
}
