import {mainContent} from "../storage.js"
import {addOrChangeQuery} from "../../handlers/handlePath.js";
import {getNewBoardsPath} from "../auxs/utils.js";


export function createElement(tagName, innerText, className, id, ...children) {
    const element = document.createElement(tagName);

    if (className != null) element.classList.add(className)
    if (id != null) element.id = id
    if (innerText != null) element.innerText = innerText

    children.forEach(child => {
        if (typeof child === "string") {
            const textNode = document.createTextNode(child)
            element.appendChild(textNode)
        } else {
            element.appendChild(child)
        }
    })

    mainContent.appendChild(element)
    return element
}

export function createRows(items, itemsPerRow) {
    const container = createElement("div",null, "boardBox-container")

    let row = createElement("div",null, "boardBox-row")

    items.forEach((item, i) => {
        row.appendChild(item)
        if ((i + 1) % itemsPerRow === 0) {
            container.appendChild(row)
            row = createElement("div",null,"boardBox-row")
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
    if(nameSearch != null) searchBar.value = nameSearch

    const selector = createElement("select", null, "search-selector")
    selector.addEventListener("change", () => {
        const selectedValue = selector.value
        if (selectedValue === "name") {
            searchBar.placeholder = "Search Board's Name"
            if(nameSearch != null) searchBar.value = nameSearch
            else searchBar.value = ""
        } else if (selectedValue === "numLists") {
            searchBar.placeholder = "Search Lists Num."
            if(numLists != null) searchBar.value = numLists
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
        document.location = getNewBoardsPath(skip, limit, nameSearch, numLists)
    })

    const nextBtn = createElement("button", "Next", "btn-secondary")
    nextBtn.classList.add("btn", "next-pagination")
    nextBtn.disabled = skip >= totalBoards - limit
    nextBtn.addEventListener("click", () => {
        skip += limit
        document.location = getNewBoardsPath(skip, limit, nameSearch, numLists)
    })

    const indices = []
    const currentPage = Math.floor(skip / limit) + 1

    for (let i = 1; i <= Math.ceil(totalBoards / limit); i++) {
        const a = createElement("a", i, "page-link")
        a.href = getNewBoardsPath((i-1)*limit, limit, nameSearch, numLists)

        const li = createElement("li", null, "page-item", null, a)
        if(i === currentPage) li.classList.add("active")

        indices.push(li)
    }

    return createElement("nav", null, "pagination-buttons", null,
        createElement("ul", null, "pagination", null,
            prevBtn, ...indices, nextBtn)
    )
}
