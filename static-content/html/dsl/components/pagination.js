import {a, button, createElement, div, input, li, nav, option, select, ul} from "../../components/elements.js";
import {addOrChangeQuery, getLimitSelectorOptions, updateBoardsPath} from "../modelAuxs.js";
import boardData from "../../../data/boardData.js";
import boardViews from "../views/boardViews.js";
import {LIMIT_INITIAL_VALUE, MAX_BOARDS_DISPLAY, SKIP_INITIAL_VALUE} from "../../../config.js";

class Pagination {
    constructor(container, skip, limit, totalBoards, nameSearch, numLists) {
        this.container = container
        this.skip = skip || SKIP_INITIAL_VALUE
        this.limit = limit || LIMIT_INITIAL_VALUE
        this.totalBoards = totalBoards
        this.nameSearch = nameSearch
        this.numLists = numLists
        this.currPage = Math.floor(this.skip / this.limit) + 1
        this.indices = []

        for (let i = 1; i <= Math.ceil(totalBoards / this.limit); i++) {
            const anchor = a(i, ["page-link"])
            const idx = li(null, ["page-item", "clickable"], null, anchor)
            idx.addEventListener("click", async () => {
                this.updatePage(i)
            })
            this.indices.push(idx)
        }
    }

    updatePage(pageNumber) {
        this.indices[this.currPage - 1].classList.remove("active")
        this.currPage = pageNumber
        this.skip = (pageNumber - 1) * this.limit
        this.prevBtn.disabled = this.currPage === 1
        this.nextBtn.disabled = this.skip >= this.totalBoards - this.limit
        this.updateIndices()
        this.fetchAndRenderBoards()
    }

    updateIndices() {
        const totalPages = Math.ceil(this.totalBoards / this.limit)
        this.indices = []
        // Generate new indices elements based on the new limit and total pages
        for (let i = 1; i <= totalPages; i++) {
            const anchor = a(i, ["page-link"])
            const idx = li(null, ["page-item", "clickable"], null, anchor)
            idx.addEventListener("click", async () => {
                this.updatePage(i)
            });
            this.indices.push(idx)
        }

        this.indices[this.currPage - 1].classList.add("active")

        document.querySelector('.pagination-indices').replaceChildren(...this.indices)
    }

    async fetchAndRenderBoards() {
        const newBoards = await boardData.getBoards(
            this.skip,
            this.limit,
            this.nameSearch,
            this.numLists
        )
        boardViews.renderBoards(
            newBoards.boards,
            this.nameSearch,
            this.numLists,
            this.container
        )
    }

    createPaginationButtons(boardsContainer) {
        this.prevBtn = button("Previous", ["btn-secondary", "btn", "prev-pagination"])
        this.nextBtn = button("Next", ["btn-secondary", "btn", "next-pagination"])

        this.prevBtn.addEventListener("click", () => {
            this.updatePage(this.currPage - 1)
        })

        this.nextBtn.addEventListener("click", () => {
            this.updatePage(this.currPage + 1)
        })


        const buttons = nav(
            null,
            ["pagination-buttons"],
            null,
            ul(null, ["pagination"], null,
                this.prevBtn,
                div(null, ["pagination-indices"], null, ...this.indices),
                this.nextBtn)
        )
        this.updatePage(1)
        return buttons
    }

    createLimitSelector() {
        const limitOptions = getLimitSelectorOptions(
            MAX_BOARDS_DISPLAY,
            5,
            this.limit
        )
        const options = limitOptions.map((option) => createElement("option", option))
        const selectHtml = select(null, [], "limit-selector", ...options)
        selectHtml.value = this.limit
        selectHtml.addEventListener("change", (ev) => {
            this.limit = ev.target.value
            this.skip = 0
            this.currPage = 1
            this.fetchAndRenderBoards()
            this.updateIndices()
            updateBoardsPath(
                this.skip,
                ev.target.value,
                this.nameSearch,
                this.numLists,
                this.totalBoards
            )
        })

        return div(
            "Boards per Page: ",
            ["pagination-limit"],
            null,
            selectHtml
        )
    }

    createSearchBar() {
        const searchBar = input(null, ["mr-sm-2", "searchBar"])
        searchBar.placeholder = "Search Board's Name"
        if (this.nameSearch != null) searchBar.value = this.nameSearch

        const selector = select(null, ["search-selector"])
        selector.addEventListener("change", () => {
            const selectedValue = selector.value
            if (selectedValue === "name") {
                searchBar.placeholder = "Search Board's Name"
                searchBar.type = "text"
                if (this.nameSearch != null) searchBar.value = this.nameSearch
                else searchBar.value = ""
            } else if (selectedValue === "numLists") {
                searchBar.placeholder = "Search Lists Num."
                searchBar.type = "number"
                if (this.numLists != null) searchBar.value = this.numLists
                else searchBar.value = ""
            }
        })

        const nameOption = option("🔠")
        nameOption.value = "name"
        const numListsOption = option("🔢")
        numListsOption.value = "numLists"

        selector.add(nameOption)
        selector.add(numListsOption)

        searchBar.addEventListener("keyup", (ev) => {
            if (ev.key === "Enter") {
                const selectedValue = selector.value
                if (selectedValue === "name" || selectedValue === "numLists") {
                    addOrChangeQuery(selectedValue, searchBar.value)
                }
            }
        })

        return div(
            null,
            ["search-selector-container"],
            null,
            selector,
            searchBar
        )
    }
}

export default Pagination
