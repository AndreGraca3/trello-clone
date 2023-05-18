import {BASE_URL, RECENT_BOARDS, user, MAX_RECENT_BOARDS, mainContent} from "../storage.js";
import {createElement} from "../components/components.js";
import {cardFunc, createCard} from "../listenerHandlers/cardFuncs.js";
import {deleteList} from "../listenerHandlers/listFuncs.js";


export function createHTMLBoard(title, description, numList, clickableFunc, color, size) {
    const card = createElement("div", null,"boardBox");
    card.classList.add("clickable");

    card.style.background = `linear-gradient(135deg, ${darkerColor(color)}, ${color})`;

    const cardBody = document.createElement("div");
    cardBody.classList.add("boardBox-body");
    cardBody.style.height = "75px"; // Set a fixed height
    cardBody.style.width = "120px"; // Set a fixed width
    card.appendChild(cardBody);

    const cardTitle = document.createElement("h5");
    cardTitle.classList.add("boardBox-title");
    cardTitle.innerText = title;
    cardBody.appendChild(cardTitle);

    const cardText = document.createElement("p1");
    cardText.classList.add("boardBox-text");
    cardText.innerText = description;
    cardBody.appendChild(cardText);

    const numListText = document.createElement("p2");
    numListText.classList.add("boardBox-text");
    numListText.innerText = numList;
    cardBody.appendChild(numListText);

    if (clickableFunc) card.addEventListener("click", clickableFunc);

    if (size) {
        card.style.border = "5px groove white";
        cardTitle.style.fontSize = `${0.2 * size}em`;
    }
    return card;
}


export function createHTMLList(list) {
    const listContainer = document.createElement("div");
    listContainer.classList.add("list-container");
    listContainer.id = `List${list.idList}`

    const listHeader = document.createElement("div")
    listHeader.classList.add("list-header")
    listHeader.innerText = list.name

    const deleteButton = document.createElement("button")
    deleteButton.classList.add("btn", "listDeleteButton")
    deleteButton.type = "button"
    deleteButton.innerText = "🗑️"
    deleteButton.addEventListener("click",async () => deleteList(list))

    listContainer.appendChild(listHeader)
    listContainer.appendChild(deleteButton)

    const listCards = document.createElement("div")
    listCards.classList.add("list-cards")
    listCards.id = `list${list.idList}`

    if (list.cards) {
        list.cards.forEach((card) => {
            if(card.archived === false) {
                const cardElement = createHTMLCard(card, () => cardFunc(card));
                listCards.appendChild(cardElement);
            }
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

    listCards.addEventListener("dragleave", (event) => {
        const dragging = document.querySelector('.dragging')
        if (!listCards.contains(event.relatedTarget)) {
            const origin = document.getElementById(`list${dragging.dataset.idList}`)
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
    cardContainer.id = `Card${card.idCard}`
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