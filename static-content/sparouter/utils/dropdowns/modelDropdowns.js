import {createElement} from "../components/components.js";
import {fetchReq} from "../auxs/utils.js";
import {cardFunc} from "../listenerHandlers/cardFuncs.js";

export async function usersDropdown(idBoard) {

    const divDrop = document.createElement("div")
    divDrop.classList.add("dropdown","dropdown-menu-user")

    const button = document.createElement("button")
    button.classList.add("btn", "btn-secondary", "dropdown-toggle", "dropdown-users")
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

export async function archivedDropdown(board) { // html board

    const button = createElement("button", "📁 Archived", "dropdown-archived", "DropdownBtn")
    button.classList.add("btn", "btn-primary", "dropdown-toggle")
    button.setAttribute("data-bs-toggle","dropdown")

    const div = createElement("div", null, "dropdown-menu-dark", "dropdownMenu-archived")
    div.classList.add("dropdown-menu")

    board.lists.forEach(
        list => {
            list.cards.forEach(
                card => {
                    if(card.archived) {
                        const getCardArchived = createElement("a", card.name, "dropdown-item",
                            `ArchivedCard${card.idCard}`)
                        getCardArchived.addEventListener("click", async () => cardFunc(card))

                        div.appendChild(getCardArchived)
                    }
                }
            )
        }
    )

    const divDrop = createElement("div", null, "dropdown-menu-archived", null, button, div)
    divDrop.classList.add("dropdown")

    return divDrop
}
