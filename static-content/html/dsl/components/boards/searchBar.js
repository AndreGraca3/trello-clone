import {div, input, option, select} from "../../../common/components/elements.js";
import {addOrChangeQuery} from "../../modelAuxs.js";

export default function createSearchBar(nameSearch, numLists) {
    const searchBar = input(null, ["mr-sm-2", "searchBar"], null, "Search Board's Name")
    if (nameSearch != null) searchBar.value = nameSearch

    const selector = select(null, ["search-selector"])
    selector.addEventListener("change", () => {
        const selectedValue = selector.value
        if (selectedValue === "name") {
            searchBar.placeholder = "Search Board's Name"
            searchBar.type = "text"
            if (nameSearch != null) searchBar.value = nameSearch
            else searchBar.value = ""
        } else if (selectedValue === "numLists") {
            searchBar.placeholder = "Search Board's Lists."
            searchBar.type = "number"
            if (numLists != null) searchBar.value = numLists
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