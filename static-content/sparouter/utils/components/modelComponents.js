import {createElement} from "./components.js";

export function createPaginationBtns(skip, limit, totalBoards) {
    const prevBtn = createElement("button", "Previous", "btn-secondary")
    prevBtn.classList.add("btn", "prev-pagination")
    prevBtn.disabled = skip === 0
    prevBtn.addEventListener("click", () => {
        skip -= limit
        document.location = `#boards?skip=${skip}&limit=${limit}`
    })

    const nextBtn = createElement("button", "Next", "btn-secondary")
    nextBtn.classList.add("btn", "next-pagination")
    nextBtn.disabled = skip >= totalBoards - limit
    nextBtn.addEventListener("click", () => {
        skip += limit
        document.location = `#boards?skip=${skip}&limit=${limit}`
    })
    return createElement("div", null, "pagination-buttons", null, prevBtn, nextBtn)
}