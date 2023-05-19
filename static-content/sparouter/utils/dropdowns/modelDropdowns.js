import {createElement} from "../components/components.js";
import {fetchReq} from "../auxs/utils.js";
import {cardFunc} from "../listenerHandlers/cardFuncs.js";

export async function usersDropdown(idBoard) {

    const button = createElement("button","🙋‍♂️ Users", "dropdown-users", "DropdownBtn")
    button.classList.add("btn", "btn-secondary", "dropdown-toggle")
    button.setAttribute("data-bs-toggle","dropdown")

    const ul = createElement("ul", null, "dropdown-menu-dark")
    ul.classList.add("dropdown-menu", "dropdown-menu-scrollable")

    const users = await fetchReq(`board/${idBoard}/allUsers`, "GET")

    users.forEach( user => {
            const img = createElement("img", null, "dropdown-item-avatar")
            img.src = user.avatar

            const li = createElement("li", null, "dropdown-item-user", null, img,
                createElement("span", user.name)
            )
            ul.appendChild(li)
        }
    )

    return createElement("div", null, "dropdown", null, button, ul)
}

export async function archivedDropdown(board) { // html board

    const button = createElement("button", "📎 Archived", "dropdown-archived", "DropdownBtn")
    button.classList.add("btn", "btn-primary", "dropdown-toggle", "dropdown-menu-scrollable")
    button.setAttribute("data-bs-toggle","dropdown")

    const ul = createElement("ul", null, "dropdown-menu-dark", "dropdownMenu-archived")
    ul.classList.add("dropdown-menu", "dropdown-menu-scrollable")

    board.lists.forEach(
        list => {
            list.cards.forEach(
                card => {
                    if(card.archived) {
                        const li = createElement("li", null, "dropdown-item",
                            `Card${card.idCard}`,
                            createElement("span", "📋 " + card.name)
                        )
                        li.addEventListener("click", async () => cardFunc(card))
                        li.classList.add("clickable")
                        ul.appendChild(li)
                    }
                }
            )
        }
    )

    const divDrop = createElement("div", null, "dropdown", null, button, ul)
    divDrop.classList.add("dropdown-archived")
    return divDrop
}
