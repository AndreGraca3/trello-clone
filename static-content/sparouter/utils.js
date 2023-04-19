export const BASE_URL = "http://localhost:8080/"

export const RECENT_BOARDS = [{name: "Board1", idBoard: 0, description: "never"},{name: "Board2",idBoard: 1, description: "used"}]

export const boardFunc = (board) => {
    document.location = `#board/${board.idBoard}`
}

export const cardFunc = (card) => {
    document.location = `#board/${card.idBoard}/list/${card.idList}/card/${card.idCard}`
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

export function createHTMLList(list) {
    const listContainer = document.createElement("div");
    listContainer.classList.add("list-container");

    const listTitle = document.createElement("h2");
    listTitle.classList.add("list-header");
    listTitle.innerText = list.name;
    listContainer.appendChild(listTitle);

    if (list.cards && Array.isArray(list.cards)) { // Add this check to see if list.cards is defined and an array
        console.log("list.cards", list.cards)
        list.cards.forEach((card) => {
            const cardElement = createHTMLCard(card, () => cardFunc(card));
            listContainer.appendChild(cardElement);
        });
    }

    return listContainer;
}
export function createHTMLCard(card, clickableFunc) {
    const cardContainer = document.createElement("div");
    cardContainer.classList.add("card-container");

    const cardContent = document.createElement("div");
    cardContent.classList.add("card-content");

    const cardTitle = document.createElement("h3");
    cardTitle.classList.add("card-title");
    cardTitle.innerText = card.name;
    cardContent.appendChild(cardTitle);

    cardContainer.appendChild(cardContent);

    if(clickableFunc) cardContainer.addEventListener("click", clickableFunc)

    // CSS for hover effect
    //cardContainer.style.transition = "background-color 0.2s ease-in-out";
    //cardContainer.style.cursor = "pointer";

    cardContainer.addEventListener("mouseover", () => {
        cardContainer.style.backgroundColor = "#ffffffff";
    });

    cardContainer.addEventListener("mouseout", () => {
        cardContainer.style.backgroundColor = "";
    });

    return cardContainer;
}



export function createHTMLHomeCard(title, description, clickableFunc, size) {
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