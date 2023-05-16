import {
    fetchReq,
}
    from "./utils/utils.js"

import {
    changeUserAvatar
}
    from "./utils/buttonFuncs.js";

import {
    BASE_URL,
    user
}
    from "./utils/storage.js";

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
            getUser(document.getElementById("mainContent"), token);
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

export const userHandler = {
    getUser,
    getLogin,
    getSignup,
    changeUserAvatar
}

export default userHandler