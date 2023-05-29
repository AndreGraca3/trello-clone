import {fetchReq} from "../utils/utils.js";


async function createList(idBoard, name) {
    return await fetchReq(`board/${idBoard}/list`, "POST", { name: name})
}

async function deleteList(idBoard, idList, action) {
    await fetchReq(`board/${idBoard}/list/${idList}${action != null ? `?action=${action}` : ''}`, "DELETE")
}

export default {
    createList,
    deleteList
}