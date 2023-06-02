import {input} from "../../components/elements.js";
import userData from "../../../data/userData.js";
import boardData from "../../../data/boardData.js";

export async function changeUserAvatar() {
    const inputHtml = document.createElement("input")
    inputHtml.type = 'file'
    inputHtml.accept = 'image/*'

    inputHtml.addEventListener('change', () => {
        const file = inputHtml.files[0]
        if (!file) return

        const reader = new FileReader()
        reader.onload = async () => {
            const imgUrl = reader.result
            if (sessionStorage.getItem("token")) {
                await userData.changeAvatar(imgUrl)
                document.querySelectorAll('.avatarImg').forEach(a => a.src = imgUrl)
            }
            document.querySelector('.avatar').src = imgUrl
        }
        reader.readAsDataURL(file)
    })

    inputHtml.click()
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
    document.querySelectorAll(".avatarImg").forEach(async e => e.src = await userData.getUserAvatar())

    const loggedOutElems = document.querySelectorAll('.loggedOut-option')
    const loggedInElems = document.querySelectorAll('.loggedIn-option')

    const token = sessionStorage.getItem('token')

    if (token) {
        loggedOutElems.forEach(e => e.style.display = "none")
        loggedInElems.forEach(e => e.style.display = "block")
    } else {
        loggedOutElems.forEach(e => e.style.display = "block")
        loggedInElems.forEach(e => e.style.display = "none")
    }
}