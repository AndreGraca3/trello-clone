import {div, h5, p1, p2} from "./elements.js";

export function coloredContainer(title, description, numList, clickableFunc, primaryColor, secondaryColor, size) {

    const cardTitle = h5(title, ["boardBox-title"])
    const cardText = p1(description, ["boardBox-text"])
    const numListText = p2(numList, ["boardBox-numLists"])

    const cardBody = div(null, ["boardBox-body"], null,
        cardTitle, cardText, numListText
    )

    const container = div(null, ["boardBox", "clickable"], null, cardBody)
    container.style.background = `linear-gradient(135deg, ${primaryColor}, ${secondaryColor})`

    if (clickableFunc) container.addEventListener("click", clickableFunc)

    if (size) {
        cardBody.style.width = "60px"
        cardBody.style.height = "30px"
        container.style.width = "fit-content"
        container.style.height = "fit-content"
        container.style.border = "5px groove white"
        cardTitle.style.fontSize = `${0.2 * size}em`
    }
    return container
}

export function createRows(items, itemsPerRow) {
    const container = div(null, ["boardBox-container"])

    let row = div(null, ["item-row"])

    items.forEach((item, i) => {
        row.appendChild(item)
        if ((i + 1) % itemsPerRow === 0) {
            container.appendChild(row)
            row = div(null, ["item-row"])
        }
    })

    // add any remaining cards to container
    if (row.children.length > 0) {
        container.appendChild(row)
    }
    return container
}
