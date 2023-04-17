import {BASE_URL, boardFunc, createHeader, createHTMLCard, createRows, RECENT_BOARDS} from "./utils.js"

function getHome(mainContent) {
    document.title = "OurTrello | Home"

    const h1 = createHeader("Welcome to OurTrello!")
    h1.classList.add("rainbow-text")
    const h2 = createHeader("🕒 Recent Boards")
    h2.style.color = "#D3D3D3"
    h2.style.fontSize = "20px"
    h2.style.paddingLeft = "10rem"
    h2.style.paddingTop = "5rem"

    const cards = RECENT_BOARDS.map(board =>
        createHTMLCard(board.name, "", () => boardFunc(board), 5)
    )

    const recent = createRows(cards, 3)
    recent.style.float = "left"
    recent.style.paddingLeft = "10rem"

    mainContent.replaceChildren(h1, h2, recent)
}

async function getUser(mainContent, token) {
    document.title = "OurTrello | User"

    const user = await (await fetch(BASE_URL + "user", {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        }
    })).json()

    const div = document.createElement("div")
    div.classList.add("text-center")

    const img = document.createElement("img")
    img.src = "https://i.imgur.com/JGtwTBw.png"
    img.style.paddingTop = "2rem"
    img.style.width = "10rem"

    const pName = document.createElement("p")
    pName.innerText = `${user.name}`

    const pEmail = document.createElement("p")
    pEmail.innerText = `${user.email}`

    div.replaceChildren(img,pName, pEmail)

    mainContent.replaceChildren(div)


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

    /** CÒDIGO REPETIDO !**/
    const h1 = document.createElement("h1")
    const text = document.createTextNode("My Boards")
    h1.replaceChildren(text)
    mainContent.replaceChildren(h1)

    const boards = await (await fetch(BASE_URL + "board", {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer token123"
        }
    })).json()

    const cards = boards.map(board => createHTMLCard(board.name, board.description, () => boardFunc(board)))

    const boardsContainer = createRows(cards, 2)
    mainContent.appendChild(boardsContainer)
}

async function getBoard(mainContent) {

    const id = document.location.hash.replace("#board/","")

    document.title = "OurTrello | board"

    fetch(BASE_URL + `board/${id}`, {
        method : "GET",
        headers: {
            "Content-type" : "application/json",
            "Authorization" : "Bearer token123"
        }
        })
        .then(resp => resp.json())
        .then( (board) => {
            const h1 = document.createElement("h1")
            const p2 = document.createElement("p")
            const p3 = document.createElement("p")
            const name = document.createTextNode(board.name)
            const desc = document.createTextNode(board.description)
            p2.replaceChildren(name)
            p3.replaceChildren(desc)
            h1.style.fontSize = "20px"
            h1.appendChild(p2)
            h1.appendChild(p3)

            const bCreate = document.createElement("button")
            const bCreateText = document.createTextNode("Create List")

            const bList = document.createElement("button")
            const bListText = document.createTextNode("Lists")

            const h2 = document.createElement("button")
            h2.addEventListener("click",() => createListForm(mainContent) )
            bCreate.replaceChildren(bCreateText)
            h2.replaceChildren(bCreate)

            const h3 = document.createElement("button")
            h3.addEventListener("click", () => document.location.hash = `#board/${id}/list`)
            bList.replaceChildren(bListText)
            h3.replaceChildren(bList)

            mainContent.replaceChildren(h1,h2,h3)
        })
        .catch(res => {
            /** O que coloco aqui? e como é que mostro erro da request?**/
            const msg = document.createTextNode(res)
            mainContent.replaceChildren(msg)
        })
}

async function getLists(mainContent) {

    const id = document.location.hash.replace("#board/","")
    const idBoard = id.replace("/list","")
    console.log(idBoard)

    document.title = "OurTrello | lists"

    const lists = await ( await fetch(BASE_URL + `board/${idBoard}/list`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer token123"
        }
    })).json()

    const temp = document.createElement("h1")

    lists.map(list => {
        const h1 = document.createElement("h1")
        const text = document.createTextNode(list.name)
        h1.replaceChildren(text)
        temp.appendChild(h1)
    })

    mainContent.replaceChildren(temp)
}

async function createListForm(mainContent) {
    document.title = "OurTrello | creating a list.."

    const id = document.location.hash.replace("#board/","")
    const idBoard = id.replace("/list","")

    const createBox = document.createElement("div");
    createBox.innerHTML = `
    <h2 style="text-align:center;">Create List</h2>
    <form id="createList-form" style="display:flex;flex-direction:column;align-items:center;">
      <label for="name">Name:</label>
      <input type="text" id="name" name="name">

      <input type="submit" value="Create" style="margin-top: 1rem;">
    </form>
  `;

    mainContent.replaceChildren(createBox)

    const form = document.getElementById("createList-form");
    form.addEventListener("submit", (event) => {
        event.preventDefault(); // prevent form from submitting and refreshing the page
        const formData = new FormData(form);
        const name = formData.get("name");
        createList(name,idBoard);
    });
}

async function createList(name,idBoard) {
    fetch(BASE_URL + `board/${idBoard}/list`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization" : "Bearer token123"
        },
        body: JSON.stringify({name}),
    })
        .then((res) => res.json())
        .then((response) => {
            // redirect to the user's page after successful sign-up
            window.location.hash = `#board/${idBoard}/list`;
        })
        .catch((err) => {
            console.error("Error creating list:", err);
        });
}

export const handlers = {
    getHome,
    getUser,
    getLogin,
    getSignup,
    getBoards,
    getBoard,
    getLists
}

export default handlers