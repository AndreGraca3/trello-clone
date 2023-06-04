import {li, span} from "../common/components/elements.js";
import {cardFunc} from "./listeners/cardFuncs.js";


export function getNextCard(container, y) {
    const draggableElements = Array.from(container.querySelectorAll('.card-container:not(.dragging)'))

    return draggableElements.reduce((closest, card) => {
        const box = card.getBoundingClientRect()
        const offset = y - box.top - box.height / 2
        if (offset < 0 && offset > closest.offset) return {offset: offset, element: card}
        else return closest
    }, {offset: Number.NEGATIVE_INFINITY}).element
}

export function moveToArchivedContainer(card, archivedContainer) {
    const newArchived = li(null, ["dropdown-item", "clickable"],
        `Card${card.idCard}`,
        span("📋 " + card.name)
    )
    newArchived.addEventListener("click", async () => cardFunc(card))

    archivedContainer.appendChild(newArchived)
}

// Changes browser URL. This doesn't fire the hashchange effect
export function updateBoardsPath(skip, limit, nameSearch, numLists, totalBoards) {
    // Fix skip and limit values
    if (skip < 0) skip = 0
    if (skip > totalBoards) skip = totalBoards - limit + 1

    const newPath = `#boards?skip=${skip}&limit=${limit}${nameSearch != null && nameSearch !== "" ? `&name=${nameSearch}` : ''}${numLists != null && numLists !== "" ? `&numLists=${numLists}` : ''}`
    window.history.pushState(null, "", newPath)
    return newPath
}

export function getLimitSelectorOptions(maxDisplay, size, limit) {
    const limitOptions = Array.from({length: size}, (_, i) => (i + 1) * maxDisplay)
    if (!limit || limitOptions.includes(limit)) return limitOptions
    limitOptions.push(limit)
    limitOptions.sort((a, b) => a - b)
    return limitOptions
}

export function addOrChangeQuery(query, value) {
    const hash = document.location.hash.split('?')
    const params = new URLSearchParams(hash[1])
    params.set(query, value)
    document.location.hash = hash[0] + '?' + params.toString()
}