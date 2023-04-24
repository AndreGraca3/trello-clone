import {cardFunc, createCard} from "./buttonFuncs.js";
import {BASE_URL, RECENT_BOARDS, user, MAX_RECENT_BOARDS} from "./storage.js";

export function createRows(items, itemsPerRow) {
    const container = document.createElement("div")
    container.classList.add("boardBox-container")

    let row = document.createElement("div")
    row.classList.add("boardBox-row")

    items.forEach((item, i) => {
        row.appendChild(item)
        if ((i + 1) % itemsPerRow === 0) {
            container.appendChild(row)
            row = document.createElement("div")
            row.classList.add("boardBox-row")
        }
    })

    // add any remaining cards to container
    if (row.children.length > 0) {
        container.appendChild(row)
    }
    return container
}

export function createHTMLBoardBox(title, description, clickableFunc, color, size) {
    const card = document.createElement("div")
    card.classList.add("boardBox")
    card.classList.add("clickable")

    card.style.background = `linear-gradient(135deg, ${darkerColor(color)}, ${color})`

    const cardBody = document.createElement("div")
    cardBody.classList.add("boardBox-body")
    card.appendChild(cardBody)

    const cardTitle = document.createElement("h5")
    cardTitle.classList.add("boardBox-title")
    cardTitle.innerText = title
    cardBody.appendChild(cardTitle)

    const cardText = document.createElement("p1")
    cardText.classList.add("boardBox-text")
    cardText.innerText = description
    cardBody.appendChild(cardText)

    card.appendChild(cardBody)

    if (clickableFunc) card.addEventListener("click", clickableFunc)

    if (size) {
        card.style.border = "5px groove white"
        cardTitle.style.fontSize = `${0.2 * size}em`
    }
    return card
}

export function createHTMLList(list) {
    const listContainer = document.createElement("div");
    listContainer.classList.add("list-container");

    const listHeader = document.createElement("div")
    listHeader.classList.add("list-header")
    listHeader.innerText = list.name

    listContainer.appendChild(listHeader)

    const listCards = document.createElement("div")
    listCards.classList.add("list-cards")
    listCards.id = `list${list.idList}`

    if (list.cards) {
        list.cards.forEach((card) => {
            const cardElement = createHTMLCard(card)
            cardElement.addEventListener("click", () => cardFunc(card, cardElement))
            listCards.appendChild(cardElement)
        })
    }
    listContainer.appendChild(listCards)

    listCards.addEventListener("dragover", (event) => {
        event.preventDefault()
        const dragging = document.querySelector('.dragging')
        const afterCard = getNextCard(listCards, event.clientY)
        if (afterCard != null) listCards.insertBefore(dragging, afterCard)
        else listCards.appendChild(dragging)
    })

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

        await fetchReq(`board/${list.idBoard}/list/${card.dataset.idList}/card/${card.dataset.idCard}`, "PUT", { idList, cix})

        card.dataset.idList = idList
    })

    const newCardButton = document.createElement("button")
    newCardButton.classList.add("newCard")
    newCardButton.innerText = "Add Card"
    newCardButton.addEventListener("click", async () => {
        await createCard(listCards, list)
    })

    listContainer.appendChild(newCardButton)

    return listContainer;
}

export function createHTMLCard(card, clickableFunc) {
    const cardContainer = document.createElement("button");
    cardContainer.classList.add("card-container");

    const cardContent = document.createElement("div");
    cardContent.classList.add("card-content");
    cardContent.innerText = card.name;
    cardContainer.appendChild(cardContent);

    cardContainer.draggable = true;
    cardContainer.addEventListener("dragstart", () => {
        cardContainer.classList.add("dragging")
    })
    cardContainer.addEventListener("dragend", () => {
        cardContainer.classList.remove("dragging")
    })
    cardContainer.dataset.idList = card.idList
    cardContainer.dataset.idCard = card.idCard

    if (clickableFunc) cardContainer.addEventListener("click", clickableFunc);

    cardContainer.classList.add("list-group-item", "list-group-item-action");

    return cardContainer;
}


export function createHeader(text) {
    const h1 = document.createElement("h1")
    const textNode = document.createTextNode(text)
    h1.replaceChildren(textNode)
    return h1
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


export async function getUserAvatar(token) {
    if (!token) return 'https://i.imgur.com/JGtwTBw.png'

    const user = await (await fetch(BASE_URL + "user", {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        }
    })).json()
    return `${user.avatar ?? 'https://i.imgur.com/JGtwTBw.png'}`
}

export function getBoardColor(idBoard) {
    const color = localStorage.getItem("color" + idBoard)
    if (!color) localStorage.setItem("color" + idBoard, randomColor())
    return localStorage.getItem("color" + idBoard)
}

export function visitBoard(board) {
    // Add visited board
    const idx = RECENT_BOARDS.indexOf(RECENT_BOARDS.find(it => it.idBoard === board.idBoard))
    if (idx !== -1) RECENT_BOARDS.splice(idx, 1)
    if (RECENT_BOARDS.length === MAX_RECENT_BOARDS) RECENT_BOARDS.pop()
    RECENT_BOARDS.unshift(board)
}

export async function fetchReq(path, method, body) {
    const options = {
        method,
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${user.token}`
        }
    }

    if (body !== undefined) {
        options.body = JSON.stringify(body)
    }

    const rsp = await fetch(BASE_URL + path, options)
    const content = await rsp.json()

    if (!rsp.ok) throw content
    return content
}

function getNextCard(container, y) {
    const draggableElements = Array.from(container.querySelectorAll('.card-container:not(.dragging)'))

    return draggableElements.reduce((closest, card) => {
        const box = card.getBoundingClientRect()
        const offset = y - box.top - box.height / 2
        if (offset < 0 && offset > closest.offset) return {offset: offset, element: card}
        else return closest
    }, {offset: Number.NEGATIVE_INFINITY}).element
}

export async function usersDropdown(idBoard) {

    const divDrop = document.createElement("div")
    divDrop.classList.add("dropdown","dropdown-menu-user")

    const button = document.createElement("button")
    button.classList.add("btn", "btn-secondary", "dropdown-toggle")
    button.setAttribute("data-bs-toggle","dropdown")
    button.ariaExpanded = "false"
    button.id = "DropdownBtn"
    button.innerText = "🙋‍♂️ Users"

    divDrop.appendChild(button)

    const ul = document.createElement("ul")
    ul.classList.add("dropdown-menu","dropdown-menu-dark")
    ul.ariaLabel = "DropdownBtn"

    const users = await fetchReq(`board/${idBoard}/allUsers`, "GET")

    users.forEach( user => {
            const li = document.createElement("li")
            li.classList.add("dropdown-item-user")

            const img = document.createElement("img")
            img.src = user.avatar
            img.classList.add("dropdown-item-avatar")

            const span = document.createElement("span")
            span.innerText = user.name

            li.appendChild(img)
            li.appendChild(span)
            ul.appendChild(li)
        }
    )

    divDrop.appendChild(ul)
    return divDrop
}