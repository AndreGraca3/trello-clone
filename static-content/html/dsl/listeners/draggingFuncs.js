import {getNextCard} from "../modelAuxs.js";
import cardData from "../../../data/cardData.js";
import {updateIdxs} from "./cardFuncs.js";

function handleDrag(event, listCards) {
    event.preventDefault()
    const dragging = document.querySelector('.dragging')
    const afterCard = getNextCard(listCards, event.clientY)
    if (afterCard != null) listCards.insertBefore(dragging, afterCard)
    else listCards.appendChild(dragging)
}

function handleDragLeave(event, listCards) {
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
}

async function handleDragDrop(event, listCards, list) {
    const card = document.querySelector('.dragging')
    const nextCard = getNextCard(listCards, event.clientY)
    let idList = listCards.id.split("list")[1]
    let dstIdx
    if (nextCard != null) {
        dstIdx = Array.from(listCards.childNodes).indexOf(nextCard)
    } else {
        dstIdx = listCards.childNodes.length
    }
    console.log(`Moved Card ${card.dataset.idCard} from list ${card.dataset.idList} to list ${idList} and cix ${dstIdx}`)

    await cardData.moveCard(list.idBoard, card.dataset.idCard, card.dataset.idList, idList, dstIdx)

    const srcList = document.getElementById(`list${card.dataset.idList}`)

    updateIdxs(srcList, listCards, card.dataset.idx, dstIdx)

    card.dataset.idList = idList
    card.dataset.idx = dstIdx
}

export default {
    handleDrag,
    handleDragLeave,
    handleDragDrop
}