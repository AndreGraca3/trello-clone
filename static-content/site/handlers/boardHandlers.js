import {LIMIT_INITIAL_VALUE, mainContent} from "../../config.js";
import boardData from "../../data/boardData.js";
import boardViews from "../views/boardViews.js";
import userData from "../../data/userData.js";


async function getBoards(args) {

    const res = await boardData.getBoards(args.skip, args.limit, args.name, args.numLists)

    boardViews.boardsPageView(res.boards, res.totalBoards, args.skip ?? 0, args.limit ?? LIMIT_INITIAL_VALUE,
        args.name, args.numLists
    )
}

async function getBoard(args) {

    const detailedBoard = await boardData.getBoard(args.idBoard)
    const boardsUsers = await userData.getAllUsers(args.idBoard, sessionStorage.getItem("token"))

    boardData.visitBoard(detailedBoard)
    boardViews.boardPageView(detailedBoard, boardsUsers)
}

export default {
    getBoards,
    getBoard
}