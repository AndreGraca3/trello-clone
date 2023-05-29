import {a, button, div, h5, input, li, nav, option, p1, p2, select, ul} from "../../components/components.js";
import {deleteList} from "../listeners/listFuncs.js";
import {cardFunc, createCard} from "../listeners/cardFuncs.js";
import {addOrChangeQuery, darkerColor, getNewBoardsPath, getNextCard} from "../modelAuxs.js";
import cardData from "../../../data/cardData.js";

export function createHTMLBoard(title, description, numList, clickableFunc, color, size) {

    const cardTitle = h5(title, ["boardBox-title"])
    const cardText = p1(description, ["boardBox-text"])
    const numListText = p2(numList, ["boardBox-numLists"])

    const cardBody = div(null, ["boardBox-body"], null,
        cardTitle, cardText, numListText
    )

    const card = div(null, ["boardBox", "clickable"], null, cardBody)
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
    const listHeader = div(list.name, ["list-header"])

    const deleteButton = button("🗑️", ["listDeleteButton", "btn"])
    deleteButton.addEventListener("click", async () => deleteList(list))

    const listCards = div(null, ["list-cards"], `list${list.idList}`)

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

export function createHTMLCard(card, clickableFunc) {

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

    return cardContainer;
}

export function createRows(items, itemsPerRow) {
    const container = div(null, ["boardBox-container"])

    let row = div(null, ["boardBox-row"])

    items.forEach((item, i) => {
        row.appendChild(item)
        if ((i + 1) % itemsPerRow === 0) {
            container.appendChild(row)
            row = div(null, ["boardBox-row"])
        }
    })

    // add any remaining cards to container
    if (row.children.length > 0) {
        container.appendChild(row)
    }
    return container
}

export function createSearchBar(nameSearch, numLists) {

    const searchBar = input(null, ["mr-sm-2", "searchBar"])
    searchBar.placeholder = "Search Board's Name"
    if (nameSearch != null) searchBar.value = nameSearch

    const selector = select(null, ["search-selector"])
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

    const nameOption = option("🔠")
    nameOption.value = "name"
    const numListsOption = option("🔢")
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

    return div(null, ["search-selector-container"], null,
        selector, searchBar)
}

export function createPaginationButtons(skip, limit, totalBoards, nameSearch, numLists) {

    const prevBtn = button("Previous", ["btn-secondary", "btn", "prev-pagination"])
    prevBtn.disabled = skip === 0
    prevBtn.addEventListener("click", () => {
        skip -= limit
        window.history.pushState(null, "", getNewBoardsPath(skip, limit, nameSearch, numLists))
    })

    const nextBtn = button("Next", ["btn-secondary", "btn", "next-pagination"])
    nextBtn.disabled = skip >= totalBoards - limit
    nextBtn.addEventListener("click", () => {
        skip += limit
        window.history.pushState(null, "", getNewBoardsPath(skip, limit, nameSearch, numLists))
    })

    const indices = []
    const currentPage = Math.floor(skip / limit) + 1

    for (let i = 1; i <= Math.ceil(totalBoards / limit); i++) {
        const anchor = a(i, ["page-link"])
        anchor.href = getNewBoardsPath((i - 1) * limit, limit, nameSearch, numLists)

        const liHtml = li(null, ["page-item"], null, anchor)
        if (i === currentPage) liHtml.classList.add("active")

        indices.push(liHtml)
    }

    return nav(null, ["pagination-buttons"], null,
        ul(null, ["pagination"], null,
            prevBtn, ...indices, nextBtn)
    )
}