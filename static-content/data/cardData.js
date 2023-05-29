import {fetchReq} from "../utils/utils.js";


async function createCard(idBoard, idList, value) {
   return await fetchReq(`board/${idBoard}/card`, "POST", {
           idList: idList,
           name: value,
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