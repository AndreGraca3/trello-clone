import {button, div, img, li, span, ul} from "../../common/components/elements.js";
import {cardFunc} from "../listeners/cardFuncs.js";
import userData from "../../../data/userData.js";

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
            liHtml.addEventListener("click", async () => cardFunc(card))
            ulHtml.appendChild(liHtml)
        }
    )

    return div(null, ["dropdown", "dropdown-archived"], null, buttonHtml, ulHtml)
}
