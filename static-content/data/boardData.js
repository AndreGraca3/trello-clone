import {fetchReq} from "../utils/utils.js";


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

export default {
    createBoard,
    getBoard,
    getBoards
}