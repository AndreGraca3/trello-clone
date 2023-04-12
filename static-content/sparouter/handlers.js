
const BASE_URL = "http://localhost:8080/"

function getHome(mainContent) {
    const h3 = document.createElement("h3")
    const text = document.createTextNode("This is OurTrello App")
    h3.replaceChildren(text)
    mainContent.replaceChildren(h3)
}
function getUser(mainContent, token) {
    const headers = new Headers({
        'Authorization': 'Bearer ' + token,
        'Content-Type': 'application/json'
    });

    fetch(BASE_URL + "user", {
        headers: headers
    })
        .then(res => res.json())
        .then(user => {
            console.log(user)
            const text = document.createTextNode(JSON.stringify(user));
            mainContent.replaceChildren(text)
        })
}

function getSignup(mainContent) {
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
    const data = { name, email };
    fetch(BASE_URL + "user", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
    })
        .then((res) => res.json())
        .then((response) => {
            const { id, token } = response;
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
            body: JSON.stringify({ email, password }),
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



export const handlers = {
    getHome,
    getUser,
    getLogin,
    getSignup
}

export default handlers