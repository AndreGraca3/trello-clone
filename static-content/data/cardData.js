import {fetchReq} from "../utils.js";


async function createCard(idBoard, idList, name) {
   return await fetchReq(`board/${idBoard}/card`, "POST", {
           idList: idList,
           name: name,
           description: null,
           endDate: null
       })
}

async function getCard(idBoard, idCard) {
    return await fetchReq(`board/${idBoard}/card/${idCard}`,"GET")
}

async function updateCard(idBoard, idCard, archived, newDescription, newEndDate, idList) {
    await fetchReq(`board/${idBoard}/card/${idCard}/update`, "PUT", {
        archived: archived,
        description: newDescription,
        endDate: newEndDate,
        idList: idList
    })
}

async function deleteCard(idBoard, idCard) {
    await fetchReq(`board/${idBoard}/card/${idCard}`, "DELETE")
}

async function moveCard(idBoard, idCard, idListNow, idListDst, cix) {
    await fetchReq(`board/${idBoard}/card/${idCard}`, "PUT", {
        idListNow: idListNow,
        idListDst: idListDst,
        cix
    })
}

export default {
    createCard,
    getCard,
    updateCard,
    deleteCard,
    moveCard
}