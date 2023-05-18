import {createElement} from "../components/components.js";
import {mainContent} from "../storage.js";

export function listModalHTML() {

    const modalHeader = createElement("div", null, "modal-header", null,
        createElement("h5", "⚠️ Warning", "modal-cardTitle", "listTitleModal")
    )

    const modalBody = createElement("div", null, "modal-body", null,
        createElement("div", "You are about to delete a list with cards, what do you want to do?")
    )

    const archiveCardsButton = createElement("button", "Archive cards", "btn-success", "listArchiveButton")
    archiveCardsButton.classList.add("btn")

    const deleteButton = createElement("button", "Delete Cards", "btn-danger", "listDeleteButton")
    deleteButton.classList.add("btn")
    deleteButton.innerText = "Delete Cards"

    const modalFooter = createElement("div", null, "modal-footer", null,
        archiveCardsButton, deleteButton
    )

    const modal = createElement("div", null, "modal", "listModal",
        createElement("div", null, "modal-dialog", null,
            createElement("div", null, "modal-content", null,
                modalHeader, modalBody, modalFooter)
        )
    )
    modal.classList.add("fade")

    mainContent.appendChild(modal)
}

export function cardModalHTML() {

    const modalDescription = createElement("div", null, "modal-cardDesc", "CardDescModal")
    modalDescription.classList.add("hide")

    const modalHeader = createElement("div", null, "modal-header", null,
        createElement("h5", null, "modal-cardTitle", "CardTitleModal"),
        createElement("h5", null, "modal-cardStartDate", "CardStartDateModal"),
        modalDescription
    )

    const time = createElement("input", null, null, "endDateTime")
    time.type = "datetime-local"

    const description = createElement("div", "Description :", "col-form-label", null,
        createElement("textarea", null, "form-control", "Description-textBox")
    )

    const modalBody = createElement("div", null, "modal-body", null,
        createElement("div", "End Date: ", "modal-body-endDate", null, time),
        description
    )

    const saveButton = createElement("button", "Save", "btn-success", "cardSaveButton")
    saveButton.classList.add("btn")

    const archiveButton = createElement("button", "Archive", "btn-primary", "cardArchiveButton")
    archiveButton.classList.add("btn")

    const deleteButton = createElement("button", "Delete", "btn-danger", "cardDeleteButton")
    deleteButton.classList.add("btn")

    const modalFooter = createElement("div", null, "modal-footer", null,
        saveButton, archiveButton, deleteButton
    )

    const modalContent = createElement("div", null, "modal-content", null,
        modalHeader, modalBody, modalFooter
    )

    const modal = createElement("div", null,"modal", "cardModal",
        createElement("div", null, "modal-dialog", null, modalContent)
    )
    modal.classList.add("fade")

    mainContent.appendChild(modal)
}