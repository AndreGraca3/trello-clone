import {
    createHeader,
    createHTMLBoardBox,
    createHTMLList,
    createRows, darkerColor, fetchReq,
    getBoardColor, getUserAvatar, usersDropdown, visitBoard
} from "./utils/utils.js"
import {boardFunc, createList} from "./utils/buttonFuncs.js";
import {BASE_URL, user, MAX_RECENT_BOARDS, MAX_BOARDS_DISPLAY, RECENT_BOARDS} from "./utils/storage.js";

function getHome(mainContent) {
    document.title = "OurTrello | Home"

    const h1 = createHeader("Welcome to OurTrello!")
    h1.classList.add("rainbow-text")

    const h2 = createHeader("🕒 Recent Boards")
    h2.style.color = "#D3D3D3"
    h2.style.fontSize = "20px"
    h2.style.paddingLeft = "10rem"
    h2.style.paddingTop = "5rem"

    const cards = RECENT_BOARDS.map(board => {
        return createHTMLBoardBox(board.name, "", () => boardFunc(board), getBoardColor(board.idBoard), 5)
    })

    const recent = createRows(cards, MAX_RECENT_BOARDS)
    recent.style.float = "left"
    recent.style.paddingLeft = "10rem"

    mainContent.replaceChildren(h1, h2, recent)
}

async function getUser(mainContent, args, token) {
    document.title = "OurTrello | User"

    const user = await fetchReq("user", "GET")

    const div = document.createElement("div")
    div.classList.add("text-center")

    const img = document.createElement("img")


    img.src = await getUserAvatar(token)
    img.style.paddingTop = "2rem"
    img.style.width = "10%"
    img.style.borderRadius = "50%"

    const pName = document.createElement("p")
    pName.innerText = `${user.name}`

    const pEmail = document.createElement("p")
    pEmail.innerText = `${user.email}`

    div.replaceChildren(img, pName, pEmail)

    mainContent.replaceChildren(div)
}

async function getCard(mainContent,args, token) {
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

    div.append(cardName, cardDescription, cardStartDate, cardEndDate, cardArchived);

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

async function getBoards(mainContent) {
    document.title = "OurTrello | Boards"

    const h1 = document.createElement("h1")
    const text = document.createTextNode("My Boards")
    h1.replaceChildren(text)
    mainContent.replaceChildren(h1)

    const boards = await fetchReq("board", "GET")

    const cards = boards.map(board => {
        return createHTMLBoardBox(board.name, board.description, () => boardFunc(board), getBoardColor(board.idBoard))
    })
    const boardsContainer = createRows(cards, MAX_BOARDS_DISPLAY)
    mainContent.appendChild(boardsContainer)
}

async function getBoard(mainContent, args, token) {
    const id = args.idBoard

    const board = await fetchReq(`board/${id}`, "GET")
    document.title = `OurTrello | ${board.name}`
    visitBoard(board)

    const boardContainer = document.createElement("div")
    boardContainer.classList.add("board")

    const desc = document.createElement("text")
    desc.innerText = board.description
    mainContent.appendChild(desc)

    board.lists.forEach(list => {
        const listContainer = createHTMLList(list)
        boardContainer.appendChild(listContainer)
    })

    const createListButton = document.createElement("button")
    createListButton.innerText = "Add new List"
    createListButton.classList.add("create-list-button")

    createListButton.addEventListener("click", () => createList(boardContainer, board))

    boardContainer.appendChild(createListButton)

    boardContainer.appendChild(await usersDropdown(board.idBoard))

    const color = getBoardColor(board.idBoard)
    mainContent.style.background = `linear-gradient(135deg, ${darkerColor(color)}, ${color})`

    mainContent.replaceChildren(boardContainer)
}

function getErrorPage(mainContent, error) {
    document.title = "OurTrello | Error"

    const h1 = createHeader("NOT THE BOARD YOU'RE LOOKING FOR")
    h1.style.color = "red"
    h1.style.fontSize = "5rem"

    const h2 = createHeader(error)

    const bg = document.createElement("div")
    bg.style.backgroundImage = `url("https://i.imgur.com/6DdhZTP.gif")`
    bg.style.opacity = "0.5"
    bg.style.backgroundPosition = "center top"
    bg.style.width = "100vw"
    bg.style.height = "50vh"

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
    getErrorPage
}

export default handlers