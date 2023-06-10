import {button, div, img, li, span, ul} from "../../common/components/elements.js";
import cardFuncs from "../listeners/cardFuncs.js";


export function usersDropdown(users) {

    const buttonHtml = button("👥 Users", ["dropdown-users", "btn", "btn-secondary", "dropdown-toggle"], "DropdownBtn")
    buttonHtml.setAttribute("data-bs-toggle", "dropdown")

    const ulHtml = ul(null, ["dropdown-menu-dark", "dropdown-menu", "dropdown-menu-scrollable"])

    users.forEach(user => {
            const imgHtml = img(null, ["dropdown-item-avatar"])
            imgHtml.src = user.avatar

            const liHtml = li(null, ["dropdown-item-user"], null, imgHtml,
                span(user.name)
            )
            ulHtml.appendChild(liHtml)
        }
    )

    return div(null, ["dropdown"], null, buttonHtml, ulHtml)
}

export function archivedDropdown(cards) { // html board

    const buttonHtml = button("📎 Archived", ["dropdown-archived", "btn", "btn-primary", "dropdown-toggle", "dropdown-menu-scrollable"], "DropdownBtn")
    buttonHtml.setAttribute("data-bs-toggle", "dropdown")

    const ulHtml = ul(null, ["dropdown-menu-dark", "dropdown-menu", "dropdown-menu-scrollable"], "dropdownMenu-archived")

    cards.forEach(
        card => {
            const liHtml = li(null, ["dropdown-item", "clickable"],
                `Card${card.idCard}`,
                span("📋 " + card.name)
            )
            liHtml.addEventListener("click", async () => cardFuncs.cardFunc(card))
            ulHtml.appendChild(liHtml)
        }
    )

    return div(null, ["dropdown", "dropdown-archived"], null, buttonHtml, ulHtml)
}

export function listDropdown(card, lists) {

    const DeArchive = button("De-archived", ["dropdown-toggle", "btn", "btn-primary", "dropdown-menu-scrollable"], "DropdownList")
    DeArchive.setAttribute("data-bs-toggle", "dropdown")

    const ulHtml = ul(null, ["dropdown-menu-dark", "dropdown-menu", "dropdown-menu-scrollable"], "dropdownMenu-lists")

    lists.forEach(
        list => {
            const listName = list.querySelector(".list-header").innerText
            const listId = list.querySelector(".list-cards").id
            const liHtml = li(listName, ["dropdown-item", "clickable"])
            liHtml.addEventListener("click", async () => cardFuncs.deArchiveCard(card, listId))
            ulHtml.appendChild(liHtml)
        }
    )

    return div(null, ["dropdown", "dropdown-archived"], "cardArchiveButton", DeArchive, ulHtml)
}