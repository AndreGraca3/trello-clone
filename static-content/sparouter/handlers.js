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
    signupBox.innerHTML = `
    <h2 style="text-align:center;">User Creation</h2>
    <form id="signup-form" style="display: flex; flex-direction: column; align-items: center;">
      <label for="name">Name:</label>
      <input type="text" id="name" name="name">

      <label for="email">Email:</label>
      <input type="text" id="email" name="email">

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


export const handlers = {
    getHome,
    getUser,
    getLogin,
    getSignup,
    getBoards
}

export default handlers