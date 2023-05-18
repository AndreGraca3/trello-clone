import {BASE_URL, MAX_RECENT_BOARDS, RECENT_BOARDS} from "../storage.js";
import {randomColor} from "./utils.js";

export async function getUserAvatar(token) {
    if (!token) return 'https://i.imgur.com/JGtwTBw.png'

    const user = await (await fetch(BASE_URL + "user", {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        }
    })).json()
    return `${user.avatar ?? 'https://i.imgur.com/JGtwTBw.png'}`
}

export function getBoardColor(idBoard) {
    const color = localStorage.getItem("color" + idBoard)
    if (!color) localStorage.setItem("color" + idBoard, randomColor())
    return localStorage.getItem("color" + idBoard)
}

export function visitBoard(board) {
    const idx = RECENT_BOARDS.indexOf(RECENT_BOARDS.find(it => it.idBoard === board.idBoard))
    if (idx !== -1) RECENT_BOARDS.splice(idx, 1)
    if (RECENT_BOARDS.length === MAX_RECENT_BOARDS) RECENT_BOARDS.pop()
    RECENT_BOARDS.unshift(board)
}