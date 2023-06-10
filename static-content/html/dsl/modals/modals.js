import {button, div, h5, input, textarea} from "../../common/components/elements.js";
import {mainContent} from "../../../config.js";


export function listModalHTML() {

    const modalHeader = div(null, ["modal-header"], null,
        h5("⚠️ Warning", ["modal-cardTitle"], "listTitleModal")
    )

    const modalBody = div(null, ["modal-body"], null,
        div("You are about to delete a list with cards, what do you want to do?")
    )

    const archiveCardsButton = button("Archive cards", ["btn-success", "btn"], "listArchiveButton")

    const deleteButton = button("Delete Cards", ["btn-danger", "btn"], "listDeleteButton")

    const modalFooter = div(null, ["modal-footer"], null,
        archiveCardsButton, deleteButton
    )

    const modal = div(null, ["modal"], "listModal",
        div(null, ["modal-dialog"], null,
            div(null, ["modal-content"], null,
                modalHeader, modalBody, modalFooter)
        )
    )
    modal.classList.add("fade")

    mainContent.appendChild(modal)
}

export function cardModalHTML() {

    const modalDescription = div(null, ["modal-cardDesc", "hide"], "CardDescModal")

    const modalHeader = div(null, ["modal-header"], null,
        h5(null, ["modal-cardTitle"], "CardTitleModal"),
        h5(null, ["modal-cardStartDate"], "CardStartDateModal"),
        modalDescription
    )

    const time = input(null, [], "endDateTime")
    time.type = "datetime-local"

    const description = div("Description :", ["col-form-label"], null,
        textarea(null, ["form-control"], "Description-textBox")
    )

    const modalBody = div(null, ["modal-body"], null,
        div("End Date: ", ["modal-body-endDate"], null, time),
        description
    )

    const saveButton = button("Save", ["btn-success", "btn"], "cardSaveButton")

    const archiveButton = button("Archive", ["btn-primary", "btn"], "cardArchiveButton")

    const deleteButton = button("Delete", ["btn-danger", "btn"], "cardDeleteButton")

    const modalFooter = div(null, ["modal-footer"], null,
        saveButton, archiveButton, deleteButton
    )

    const modalContent = div(null, ["modal-content"], null,
        modalHeader, modalBody, modalFooter
    )

    const modal = div(null,["modal"], "cardModal",
        div(null, ["modal-dialog"], null, modalContent)
    )
    modal.classList.add("fade")

    mainContent.appendChild(modal)
}