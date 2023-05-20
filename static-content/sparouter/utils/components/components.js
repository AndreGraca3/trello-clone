import {mainContent} from "../storage.js"
import {addOrChangeQuery} from "../../handlers/handlePath.js";


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

export function createSearchBar() {
    const searchBar = createElement("input", null, "mr-sm-2")
    searchBar.classList.add("searchBar")
    searchBar.placeholder = "Search Board's Name"

    const selector = createElement("select", null, "search-selector")
    selector.addEventListener("change", () => {
        const selectedValue = selector.value
        if (selectedValue === "name") {
            searchBar.placeholder = "Search Board's Name"
        } else if (selectedValue === "numLists") {
            searchBar.placeholder = "Search Lists Num."
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
