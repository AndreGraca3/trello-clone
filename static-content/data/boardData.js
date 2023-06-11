import {fetchReq} from "../utils.js";
import {MAX_RECENT_BOARDS} from "../config.js";


async function createBoard(boardName, boardDesc) {
    return await fetchReq("board", "POST", {name: boardName, description: boardDesc})
}

async function getBoard(idBoard) {
    return await fetchReq(`board/${idBoard}`, "GET")
}

async function getBoards(skip, limit, name, numLists) {
    return await fetchReq(`board?skip=${skip}&limit=${limit}${name != null ? `&name=${name}` : ''}${numLists != null ? `&numLists=${numLists}` : ''}`,
        "GET")
}

async function addUserToBoard(idBoard, userEmail) {
    return await fetchReq(`board/${idBoard}`, "PUT", {userEmail: userEmail})
}

export let RECENT_BOARDS = []

function clearRecentBoards() {
    RECENT_BOARDS = []
}

function visitBoard(board) {
    const idx = RECENT_BOARDS.indexOf(RECENT_BOARDS.find(it => it.idBoard === board.idBoard))
    if (idx !== -1) RECENT_BOARDS.splice(idx, 1)
    if (RECENT_BOARDS.length === MAX_RECENT_BOARDS) RECENT_BOARDS.pop()
    RECENT_BOARDS.unshift(board)
}

export default {
    clearRecentBoards,
    visitBoard,
    createBoard,
    getBoard,
    getBoards,
    addUserToBoard
}