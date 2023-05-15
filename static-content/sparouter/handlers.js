import {
    createHeader,
    createHTMLBoardBox,
    createHTMLList,
    createRows, darkerColor, fetchReq,
    getBoardColor, getUserAvatar, visitBoard, usersDropdown, cardModalHTML, archivedDropdown, listModalHTML
} from "./utils/utils.js"
import {boardFunc, createList, changeUserAvatar} from "./utils/buttonFuncs.js";
import {BASE_URL, user, MAX_RECENT_BOARDS, MAX_BOARDS_DISPLAY, RECENT_BOARDS, LIMIT_INITIAL_VALUE} from "./utils/storage.js";

function getHome(mainContent) {
    document.title = "OurTrello | Home"

    const h1 = createHeader("Welcome to OurTrello!")
    h1.classList.add("rainbow-text")

    const h2 = createHeader("🕒 Recent Boards")
    h2.classList.add("recent-boards")

    const recentBoards = RECENT_BOARDS.map(board => {
        return createHTMLBoardBox(board.name, "", "", () => boardFunc(board), getBoardColor(board.idBoard), 5)
    })

    const recent = createRows(recentBoards, MAX_RECENT_BOARDS)
    recent.classList.add("recent-rows")

    mainContent.replaceChildren(h1, h2, recent)
}

async function getUser(mainContent, args, token) {
    document.title = "OurTrello | User";

    const user = await fetchReq("user", "GET");

    const div = document.createElement("div");
    div.classList.add("text-center");

    const img = document.createElement("img");
    img.classList.add("avatar")

    img.src = user.avatar //await getUserAvatar(token)

    img.addEventListener("click", async () => {
          await changeUserAvatar(token);
    });

    const pName = document.createElement("p");
    pName.innerText = `${user.name}`;

    const pEmail = document.createElement("p");
    pEmail.innerText = `${user.email}`;

    div.replaceChildren(img, pName, pEmail);

    mainContent.replaceChildren(div);
}


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
    button.classList.add("btn","btn-success")
    button.innerText = "📁"
    button.addEventListener("click", async () => {
        const changes = {
            archived : false,
            description: card.description,
            endDate: card.endDate
        }

        await fetchReq(`board/${idBoard}/list/${idList}/card/${idCard}/updateCard`, "PUT", changes)
        document.location = `#board/${idBoard}`
    })

    div.append(cardName, cardDescription, cardStartDate, cardEndDate, cardArchived,button);

    mainContent.replaceChildren(div);

}

function getSignup(mainContent) {   // Whats this compared to below function?

    const signupBox = document.createElement("div");
    signupBox.classList.add("signup-box");
    /** Refazer isto no futuro! **/
    signupBox.innerHTML = `
    <h2 style="text-align:center;">User Creation</h2>
    <form id="signup-form" style="display: flex; flex-direction: column; align-items: center;">
      <label for="name">Name:</label>
      <input type="text" id="name" name="name">

      <label for="email">Email:</label>
      <input type="text" id="email" name="email" required>

      <label for="password">Password:</label>
      <input type="password" id="password" name="password">

      <input type="submit" value="Sign Up">
    </form>
  `;
    mainContent.replaceChildren(signupBox);

    const form = document.getElementById("signup-form");
    form.addEventListener("submit", (event) => {
        event.preventDefault(); // prevent form from submitting and refreshing the page
        const formData = new FormData(form);
        const name = formData.get("name");
        const email = formData.get("email");
        const password = formData.get("password");
        createUser(name, email, password);
    });
}

function createUser(name, email, password) {
    const data = {name, email};
    fetch(BASE_URL + "user", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(data),
    })
        .then((res) => res.json())
        .then((response) => {
            const {id, token} = response;
            console.log(`New user created with ID ${id} and token ${token}`);

            // redirect to the user's page after successful sign-up
            window.location.hash = "#user";
            handlers.getUser(document.getElementById("mainContent"), token);
        })
        .catch((err) => {
            console.error("Error creating user:", err);
        });
}

function getLogin(mainContent) {
    document.title = "OurTrello | Login"

    const loginBox = document.createElement("div");
    loginBox.classList.add("login-box");
    loginBox.innerHTML = `
    <h2 style="text-align:center;">Login</h2>
    <form id="login-form" style="display:flex;flex-direction:column;align-items:center;">
      <label for="email">Email:</label>
      <input type="text" id="email" name="email">

      <label for="password">Password:</label>
      <input type="password" id="password" name="password">

      <input type="submit" value="Log In" style="margin-top: 1rem;">
    </form>
  `;
    mainContent.replaceChildren(loginBox);

    const loginForm = document.getElementById("login-form");
    loginForm.addEventListener("submit", async (event) => {
        //event.preventDefault();
        TODO("Implement login functionality")
        const formData = new FormData(loginForm);
        const email = formData.get("email");
        const password = formData.get("password");

        // Send a request to the server to authenticate the user
        const response = await fetch(BASE_URL + "user", {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({email, password}),
        });

        if (response.ok) {
            // Authentication successful, redirect to the #user page
            window.location.href = "#user";
        } else {
            // Authentication failed, show an error message
            const errorBox = document.createElement("div");
            errorBox.textContent = "Incorrect email or password";
            loginForm.appendChild(errorBox);
        }
    });
}

async function getBoards(mainContent, args) {

    document.title = "OurTrello | Boards"

    console.log(args)
    const h1 = createHeader("My Boards")
    mainContent.replaceChildren(h1)

    if(args.skip === undefined) { args.skip = 0 }
    if(args.limit === undefined) { args.limit = LIMIT_INITIAL_VALUE }

    const boards = await fetchReq(`board?skip=${args.skip}&limit=${args.limit}`, "GET")
    document.location = `#boards?skip=${args.skip}&limit=${args.limit}`

    const cards = boards.map(board => {
        console.log(board)
        return createHTMLBoardBox(board.name, board.description, board.numLists, () => boardFunc(board), getBoardColor(board.idBoard))
    })

    const boardsContainer = createRows(cards, MAX_BOARDS_DISPLAY)

    /** Pagination Buttons**/
    const paginationContainer = document.createElement("div")
    paginationContainer.classList.add("pagination")

    const prevBtn = document.createElement("button")
    prevBtn.classList.add("btn", "btn-secondary")
    prevBtn.textContent = "Previous"
    prevBtn.disabled = args.skip === 0
    prevBtn.addEventListener("click", () => {
        args.skip -= args.limit
        getBoards(mainContent, args)
    })

    const nextBtn = document.createElement("button")
    nextBtn.classList.add("btn", "btn-secondary")
    nextBtn.textContent = "Next"
    nextBtn.disabled = cards.length < MAX_BOARDS_DISPLAY
    nextBtn.addEventListener("click", () => {
        args.skip += args.limit
        getBoards(mainContent, args)
    })

    // Add margin to the buttons
    prevBtn.style.marginRight = "5px"
    nextBtn.style.marginLeft = "5px"

    // Center the buttons
    paginationContainer.style.display = "flex"
    paginationContainer.style.justifyContent = "center"

    paginationContainer.appendChild(prevBtn)
    paginationContainer.appendChild(nextBtn)
    mainContent.appendChild(paginationContainer)
    mainContent.appendChild(boardsContainer)
}


async function getBoard(mainContent, args) {
    const id = args.idBoard

    const board = await fetchReq(`board/${id}`, "GET")
    document.title = `OurTrello | ${board.name}`
    visitBoard(board)

    const boardHeader = document.createElement("h5")

    const desc = document.createElement("p")
    desc.innerText = board.description.charAt(0).toUpperCase() + board.description.slice(1)
    desc.classList.add("board-desc")

    const archived = await archivedDropdown(board)

    const users = await usersDropdown(board.idBoard)

    boardHeader.replaceChildren(desc, users, archived)

    const boardContainer = document.createElement("div")
    boardContainer.classList.add("board")
    boardContainer.id = "boardContainer"


    board.lists.forEach(list => {
        const listContainer = createHTMLList(list)
        boardContainer.appendChild(listContainer)
    })

    const createListButton = document.createElement("button")
    createListButton.innerText = "Add new List"
    createListButton.classList.add("create-list-button")

    createListButton.addEventListener("click", () => createList(boardContainer, board))

    boardContainer.appendChild(createListButton)

    const color = getBoardColor(board.idBoard)
    mainContent.style.background = `linear-gradient(135deg, ${darkerColor(color)}, ${color})`

    const cardModal = cardModalHTML()
    const listModal = listModalHTML()

    mainContent.replaceChildren(boardHeader, boardContainer, cardModal, listModal)
}

function getErrorPage(mainContent, error) {
    document.title = "OurTrello | Error"

    const h1 = createHeader("NOT THE BOARD YOU'RE LOOKING FOR")
    h1.classList.add("error-header")

    const h2 = createHeader(error)

    const bg = document.createElement("div")
    bg.classList.add("bg-error")

    mainContent.replaceChildren(h1, h2, bg)
}

export const handlers = {
    getHome,
    getUser,
    getLogin,
    getSignup,
    getBoards,
    getBoard,
    getCard,
    getErrorPage,
    changeUserAvatar
}

export default handlers