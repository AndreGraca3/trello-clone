import {button, div, img, li, span, ul} from "../../components/elements.js";
import {cardFunc} from "../listeners/cardFuncs.js";
import userData from "../../../data/userData.js";

export async function usersDropdown(idBoard) {

    const buttonHtml = button("👥 Users", ["dropdown-users", "btn", "btn-secondary", "dropdown-toggle"], "DropdownBtn")
    buttonHtml.setAttribute("data-bs-toggle", "dropdown")

    const ulHtml = ul(null, ["dropdown-menu-dark", "dropdown-menu", "dropdown-menu-scrollable"])

    const users = await userData.getAllUsers(idBoard)

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

export async function archivedDropdown(board) { // html board

    const buttonHtml = button("📎 Archived", ["dropdown-archived", "btn", "btn-primary", "dropdown-toggle", "dropdown-menu-scrollable"], "DropdownBtn")
    buttonHtml.setAttribute("data-bs-toggle", "dropdown")

    const ulHtml = ul(null, ["dropdown-menu-dark", "dropdown-menu", "dropdown-menu-scrollable"], "dropdownMenu-archived")

    board.archivedCards.forEach(
        card => {
            if (card.archived) {
                const liHtml = li(null, ["dropdown-item", "clickable"],
                    `Card${card.idCard}`,
                    span("📋 " + card.name)
                )
                liHtml.addEventListener("click", async () => cardFunc(card))
                ulHtml.appendChild(liHtml)
            }
        }
    )

    return div(null, ["dropdown", "dropdown-archived"], null, buttonHtml, ulHtml)
}
