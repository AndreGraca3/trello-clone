export const BASE_URL = "http://localhost:8080/"

export const RECENT_BOARDS = [{name: "Board1", idBoard: 0, description: "never"},{name: "Board2",idBoard: 1, description: "used"}]

export const boardFunc = (board) => {
    document.location = `#board/${board.idBoard}`
}

export function createRows(items, itemsPerRow) {
    const container = document.createElement("div")
    container.classList.add("card-container")

    let row = document.createElement("div")
    row.classList.add("card-row")

    items.forEach((item, i) => {
        row.appendChild(item)
        if ((i + 1) % itemsPerRow === 0) {
            container.appendChild(row)
            row = document.createElement("div")
            row.classList.add("card-row")
        }
    })

    // add any remaining cards to container
    if (row.children.length > 0) {
        container.appendChild(row)
    }
    return container
}

export function createHTMLCard(title, description, clickableFunc, size) {
    const card = document.createElement("div")
    card.classList.add("card")
    card.classList.add("clickable")

    card.style.background = `linear-gradient(135deg, ${randomColor()}, ${randomColor()})`
    card.style.width = "15em"
    card.style.border = "15px groove white"

    const cardBody = document.createElement("div")
    cardBody.classList.add("card-body")
    card.appendChild(cardBody)

    const cardTitle = document.createElement("h5")
    cardTitle.classList.add("card-title")
    cardTitle.innerText = title
    cardTitle.style.fontStyle = "italic"
    cardBody.appendChild(cardTitle)

    const cardText = document.createElement("p1")
    cardText.classList.add("card-text")
    cardText.innerText = description
    cardText.style.textDecoration = "underline"
    cardBody.appendChild(cardText)

    card.appendChild(cardBody)

    if(clickableFunc) card.addEventListener("click", clickableFunc)

    if(size) {
        card.style.width = `${size}em`
        card.style.border = "5px groove white"
        cardTitle.style.fontSize = `${0.2 * size}em`
        cardTitle.style.whiteSpace = "nowrap"
    }
    return card
}

export function createHeader(text) {
    const h1 = document.createElement("h1")
    const textNode = document.createTextNode(text)
    h1.replaceChildren(textNode)
    return h1
}

export function randomColor() {
    let r, g, b
    do {
        r = Math.floor(Math.random() * 256)
        g = Math.floor(Math.random() * 256)
        b = Math.floor(Math.random() * 256)
    } while (r + g + b < 250)
    return `rgb(${r}, ${g}, ${b})`
}