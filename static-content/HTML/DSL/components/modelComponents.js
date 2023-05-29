import {createElement} from "../../components/components.js";
import {deleteList} from "../listeners/listFuncs.js";
import {cardFunc, createCard} from "../listeners/cardFuncs.js";
import {addOrChangeQuery, darkerColor, getNewBoardsPath, getNextCard} from "../modelAuxs.js";
import {fetchReq} from "../../../utils/utils.js";

export function createHTMLBoard(title, description, numList, clickableFunc, color, size) {

    const cardTitle = createElement("h5", title, "boardBox-title")
    const cardText = createElement("p1", description, "boardBox-text")
    const numListText = createElement("p2", numList, "boardBox-numLists")

    const cardBody = createElement("div", null, "boardBox-body", null,
        cardTitle, cardText, numListText
    )

    const card = createElement("div", null, "boardBox", null, cardBody)
    card.classList.add("clickable")
    card.style.background = `linear-gradient(135deg, ${darkerColor(color)}, ${color})`

    if (clickableFunc) card.addEventListener("click", clickableFunc)

    if (size) {
        cardBody.style.width = "60px"
        cardBody.style.height = "30px"
        card.style.width = "fit-content"
        card.style.height = "fit-content"
        card.style.border = "5px groove white"
        cardTitle.style.fontSize = `${0.2 * size}em`
    }
    return card
}

export function createHTMLList(list) {
    const listHeader = createElement("div", list.name, "list-header")

    const deleteButton = createElement("button", "🗑️", "listDeleteButton")
    deleteButton.classList.add("btn")
    deleteButton.addEventListener("click", async () => deleteList(list))

    const listCards = createElement("div", null, "list-cards", `list${list.idList}`)

    if (list.cards) {
        list.cards.forEach((card) => {
            if (!card.archived) {
                const cardElement = createHTMLCard(card, () => cardFunc(card));
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

        const body = {
            idListNow: card.dataset.idList,
            idListDst: idList,
            cix
        }

        await fetchReq(`board/${list.idBoard}/card/${card.dataset.idCard}`, "PUT", body)

        card.dataset.idList = idList
        card.dataset.idx = cix
    })

    const newCardButton = createElement("button", "Add Card", "newCard")
    newCardButton.addEventListener("click", async () => {
        await createCard(listCards, list)
    })

    return createElement("div", null, "list-container", `List${list.idList}`,
        listHeader, deleteButton, listCards, newCardButton
    )
}

export function createHTMLCard(card, clickableFunc) {

    const cardContainer = createElement("button", null, "card-container", `Card${card.idCard}`,
        createElement("div", card.name, "card-content")
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

    return cardContainer;
}

export function createRows(items, itemsPerRow) {
    const container = createElement("div", null, "boardBox-container")

    let row = createElement("div", null, "boardBox-row")

    items.forEach((item, i) => {
        row.appendChild(item)
        if ((i + 1) % itemsPerRow === 0) {
            container.appendChild(row)
            row = createElement("div", null, "boardBox-row")
        }
    })

    // add any remaining cards to container
    if (row.children.length > 0) {
        container.appendChild(row)
    }
    return container
}

export function createSearchBar(nameSearch, numLists) {

    const searchBar = createElement("input", null, "mr-sm-2")
    searchBar.classList.add("searchBar")
    searchBar.placeholder = "Search Board's Name"
    if (nameSearch != null) searchBar.value = nameSearch

    const selector = createElement("select", null, "search-selector")
    selector.addEventListener("change", () => {
        const selectedValue = selector.value
        if (selectedValue === "name") {
            searchBar.placeholder = "Search Board's Name"
            if (nameSearch != null) searchBar.value = nameSearch
            else searchBar.value = ""
        } else if (selectedValue === "numLists") {
            searchBar.placeholder = "Search Lists Num."
            if (numLists != null) searchBar.value = numLists
            else searchBar.value = ""
        }
    })

    const nameOption = createElement("option", "🔠")
    nameOption.value = "name"
    const numListsOption = createElement("option", "🔢")
    numListsOption.value = "numLists"

    selector.add(nameOption)
    selector.add(numListsOption)

    searchBar.addEventListener("keyup", (ev) => {
        if (ev.key === "Enter") {
            const selectedValue = selector.value
            if (selectedValue === "name" || selectedValue === "numLists")
                addOrChangeQuery(selectedValue, searchBar.value)
        }
    })

    return createElement("div", null, "search-selector-container", null,
        selector, searchBar)
}

export function createPaginationButtons(skip, limit, totalBoards, nameSearch, numLists) {

    const prevBtn = createElement("button", "Previous", "btn-secondary")
    prevBtn.classList.add("btn", "prev-pagination")
    prevBtn.disabled = skip === 0
    prevBtn.addEventListener("click", () => {
        skip -= limit
        window.history.pushState(null, "", getNewBoardsPath(skip, limit, nameSearch, numLists))
    })

    const nextBtn = createElement("button", "Next", "btn-secondary")
    nextBtn.classList.add("btn", "next-pagination")
    nextBtn.disabled = skip >= totalBoards - limit
    nextBtn.addEventListener("click", () => {
        skip += limit
        window.history.pushState(null, "", getNewBoardsPath(skip, limit, nameSearch, numLists))
    })

    const indices = []
    const currentPage = Math.floor(skip / limit) + 1

    for (let i = 1; i <= Math.ceil(totalBoards / limit); i++) {
        const a = createElement("a", i, "page-link")
        a.href = getNewBoardsPath((i - 1) * limit, limit, nameSearch, numLists)

        const li = createElement("li", null, "page-item", null, a)
        if (i === currentPage) li.classList.add("active")

        indices.push(li)
    }

    return createElement("nav", null, "pagination-buttons", null,
        createElement("ul", null, "pagination", null,
            prevBtn, ...indices, nextBtn)
    )
}