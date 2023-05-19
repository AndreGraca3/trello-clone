import {mainContent} from "../storage.js"


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