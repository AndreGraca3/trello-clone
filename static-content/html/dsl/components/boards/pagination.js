import {a, button, createElement, div, input, li, nav, option, select, ul} from "../../../common/components/elements.js";
import {getLimitSelectorOptions, updateBoardsPath} from "../../modelAuxs.js";
import boardData from "../../../../data/boardData.js";
import boardViews from "../../../../site/views/boardViews.js";
import {LIMIT_INITIAL_VALUE, MAX_BOARDS_DISPLAY, SKIP_INITIAL_VALUE} from "../../../../config.js";
import router from "../../../../router.js";

class Pagination {
    constructor(container, skip, limit, totalBoards) {
        this.container = container
        this.skip = skip || SKIP_INITIAL_VALUE
        this.limit = limit || LIMIT_INITIAL_VALUE
        this.totalBoards = totalBoards
        const queryParams = {args: {}, handler: null}
        router.getQueryParams(document.location.hash, queryParams)
        this.nameSearch = queryParams.args["name"]
        this.numLists = queryParams.args["numLists"]
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
        if (this.indices.length !== 0) this.indices[this.currPage - 1].classList.remove("active")
        this.currPage = pageNumber
        this.skip = (pageNumber - 1) * this.limit
        this.prevBtn.disabled = this.currPage === 1
        this.nextBtn.disabled = this.skip >= this.totalBoards - this.limit
        this.updateIndices()
        this.fetchAndRenderBoards()
        updateBoardsPath(this.skip, this.limit, this.nameSearch, this.numLists, this.totalBoards)
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

        if (this.indices.length !== 0) this.indices[this.currPage - 1].classList.add("active")

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

    createPaginationButtons() {
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
        this.updatePage(this.currPage)
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
}

export default Pagination
