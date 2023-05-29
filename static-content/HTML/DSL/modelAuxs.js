import {MAX_RECENT_BOARDS, RECENT_BOARDS} from "../../config/storage.js";
import {createElement, li, span} from "../components/components.js";
import {cardFunc} from "./listeners/cardFuncs.js";

export function getBoardColor(idBoard) {
    const color = localStorage.getItem("color" + idBoard)
    if (!color) localStorage.setItem("color" + idBoard, randomColor())
    return localStorage.getItem("color" + idBoard)
}

export function visitBoard(board) {
    const idx = RECENT_BOARDS.indexOf(RECENT_BOARDS.find(it => it.idBoard === board.idBoard))
    if (idx !== -1) RECENT_BOARDS.splice(idx, 1)
    if (RECENT_BOARDS.length === MAX_RECENT_BOARDS) RECENT_BOARDS.pop()
    RECENT_BOARDS.unshift(board)
}

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

export function getNewBoardsPath(skip, limit, nameSearch, numLists) {
    return `#boards?skip=${skip}&limit=${limit}${nameSearch != null && nameSearch !== "" ? `&name=${nameSearch}` : ''}${numLists != null && numLists !== "" ? `&numLists=${numLists}` : ''}`
}

export function getLimitSelectorOptions(maxDisplay, size, limit) {
    const limitOptions = Array.from({length: size}, (_, i) => (i + 1) * maxDisplay)
    if (!limit || limitOptions.includes(limit)) return limitOptions
    limitOptions.push(limit)
    limitOptions.sort((a, b) => a - b)
    return limitOptions
}


export function randomColor() {
    const rgb = Array.from({length: 3}, () => Math.floor(Math.random() * 256));
    return `rgb(${rgb.join(', ')})`;
}

export function darkerColor(color) {
    const [r, g, b] = color.slice(4, -1).split(", ");
    const newR = Math.max(Number(r) - 100, 0);
    const newG = Math.max(Number(g) - 100, 0);
    const newB = Math.max(Number(b) - 100, 0);
    return `rgb(${newR}, ${newG}, ${newB})`;
}

export function addOrChangeQuery(query, value) {
    const hash = document.location.hash.split('?')
    const params = new URLSearchParams(hash[1])
    params.set(query, value)
    document.location.hash = hash[0] + '?' + params.toString()
}