import {fetchReq} from "./utils/utils.js";


async function getCard(mainContent, args, token) {
    const idBoard = args.idBoard;
    const idList = args.idList;
    const idCard = args.idCard;

    document.title = `OurTrello | Card`;

    const card = await fetchReq(`board/${idBoard}/list/${idList}/card/${idCard}`, "GET")

    const div = document.createElement("div");
    div.classList.add("text-center");

    const cardName = document.createElement("h2");
    cardName.innerText = `${card.name}`;

    const cardDescription = document.createElement("p");
    cardDescription.innerText = `Description: ${card.description}`;

    const cardStartDate = document.createElement("p");
    cardStartDate.innerText = `Start Date: ${card.startDate}`;

    const cardEndDate = document.createElement("p");
    cardEndDate.innerText = `End Date: ${card.endDate || "Not set"}`;

    const cardArchived = document.createElement("p");
    cardArchived.innerText = `Archived: ${card.archived}`;

    const button = document.createElement("button")
    button.classList.add("btn", "btn-success")
    button.innerText = "📁"
    button.addEventListener("click", async () => {
        const changes = {
            archived: false,
            description: card.description,
            endDate: card.endDate
        }

        await fetchReq(`board/${idBoard}/list/${idList}/card/${idCard}/updateCard`, "PUT", changes)
        document.location = `#board/${idBoard}`
    })

    div.append(cardName, cardDescription, cardStartDate, cardEndDate, cardArchived, button);

    mainContent.replaceChildren(div);

}

export const cardHandler = {
    getCard
}

export default cardHandler