import userData from "../../../data/userData.js";
import boardData from "../../../data/boardData.js";
import {fileInput} from "../../common/components/elements.js";

export async function changeUserAvatar() {
    const handler = async (reader) => {
        const imgUrl = reader.result
        if (sessionStorage.getItem("token")) {
            await userData.changeAvatar(imgUrl)
            document.querySelectorAll('.avatarImg').forEach(a => a.src = imgUrl)
        }
        document.querySelector('.avatar').src = imgUrl
    }

    fileInput(handler)
}

export async function createUser(name, email, password, urlAvatar) {
    const res = await userData.createUser(name, email, password, urlAvatar)
    sessionStorage.setItem("token", res.token)
    updateUIElements()
    document.location = "#user"
}

export async function loginUser(email, password) {
    const token = await userData.login(email, password)
    sessionStorage.setItem("token", token)
    updateUIElements()
    document.location = "#user"
}

export function logout() {
    sessionStorage.removeItem('token')
    boardData.clearRecentBoards()
    updateUIElements()
    document.location = "#home"
    const hashChangeEvent = new HashChangeEvent('hashchange')
    window.dispatchEvent(hashChangeEvent)
}

export function updateUIElements() {
    const token = sessionStorage.getItem("token")
    document.querySelectorAll(".avatarImg").forEach(async e =>
        e.src = await userData.getUserAvatar(token)
    )

    const loggedOutElems = document.querySelectorAll('.loggedOut-option')
    const loggedInElems = document.querySelectorAll('.loggedIn-option')

    if (token) {
        loggedOutElems.forEach(e => e.style.display = "none")
        loggedInElems.forEach(e => e.style.display = "block")
    } else {
        loggedOutElems.forEach(e => e.style.display = "block")
        loggedInElems.forEach(e => e.style.display = "none")
    }
}