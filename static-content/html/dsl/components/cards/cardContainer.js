import {button, div} from "../../../common/components/elements.js";

export default function cardContainer(card, clickableFunc) {

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
