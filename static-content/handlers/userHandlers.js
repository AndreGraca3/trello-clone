import {changeUserAvatar, createUser, loginUser} from "../HTML/DSL/listeners/userFuncs.js";
import {button, createElement, div, form, h1, img, input, label, p} from "../HTML/components/components.js";
import {mainContent} from "../config/storage.js";
import userData from "../data/userData.js";

async function getUser(args, token) {

    document.title = "OurTrello | User";

    const user = await userData.getUser()

    const imgHtml = img(null, ["avatar", "avatarImg"])
    //img.src = "https://i.imgur.com/6VBx3io.png"; //TODO: change to user avatar
    imgHtml.src = user.avatar
    imgHtml.addEventListener("click", async () => {
        await changeUserAvatar(sessionStorage.getItem("token"));
    });

    div(null, ["text-center"], null,
        imgHtml,
        p(`${user.name}`),
        p(`${user.email}`)
    )
}

async function getSignup() {
    document.title = "OurTrello | Signup";

    h1("Sign Up in OurTrello", ["text-center"]);

    const formContainer = div( null, ["form-container"]);

    const formHtml = form(null, ["signup-form"]);

    const inputs = {};

    const nameRow = createRow("Name:", "form-input", "nameInput", inputs);
    formHtml.appendChild(nameRow);

    const emailRow = createRow("Email:", "form-input", "emailInput", inputs);
    formHtml.appendChild(emailRow);

    const passwordRow = createRow("Password:", "form-input", "passwordInput", inputs);
    passwordRow.querySelector("input").type = "password";
    const confirmPasswordRow = createRow("Confirm Password:", "form-input", "confirmPasswordInput", inputs);
    confirmPasswordRow.querySelector("input").type = "password";
    formHtml.appendChild(passwordRow);
    formHtml.appendChild(confirmPasswordRow);

    const logo = img(null, ["avatar", "avatarImg"]);
    logo.src = "https://i.imgur.com/6VBx3io.png"; //TODO: change to user avatar
    logo.style.height = "150px";
    logo.style.width = "150px"

    logo.addEventListener("click", async () => {
        await changeUserAvatar();
    });
    formHtml.appendChild(logo);

    const submit = button("Submit", ["btn-secondary"]);
    submit.style.marginTop = "175px";

    submit.addEventListener("click", async () => {

        const name = inputs["nameInput"].value;
        const email = inputs["emailInput"].value;
        const password = inputs["passwordInput"].value;
        const confirmPassword = inputs["confirmPasswordInput"].value;

        if (password !== confirmPassword) {
            alert("Passwords don't match");
            return;
        }
        await createUser(name, email, password, logo.src)
    });

    formContainer.appendChild(formHtml);
    formContainer.appendChild(submit);
    mainContent.appendChild(formContainer);
}

async function getLogin() {
    document.title = "OurTrello | Login";

    h1("Log In to OurTrello", ["text-center"]);

    const formContainer = div( null, ["form-container"]);

    const formHtml = form(null, ["login-form"]);

    const inputs = {};

    const emailRow = createRow("Email:", "form-input", "emailInput", inputs);
    formHtml.appendChild(emailRow);

    const passwordRow = createRow("Password:", "form-input", "passwordInput", inputs);
    passwordRow.querySelector("input").type = "password";
    formHtml.appendChild(passwordRow);

    const submit = button("Log In", ["btn-primary"]);
    submit.style.marginTop = "20px"

    submit.addEventListener("click", async () => {
        const email = inputs["emailInput"].value;
        const password = inputs["passwordInput"].value;

        await loginUser(email, password);
    });

    formContainer.appendChild(formHtml);
    formContainer.appendChild(submit);

    mainContent.appendChild(formContainer);
}

async function logout() {
    sessionStorage.setItem('token', null)
    document.querySelectorAll(".avatarImg").forEach(e => e.src = "https://i.imgur.com/6VBx3io.png")
    //showLogoutModal()
    document.querySelector('#user-option').style.display = "none"
    document.querySelector('#logout-option').style.display = "none"
    document.location = "#home"
}
function createRow(labelText, inputClassName, inputName, inputs) {
    const row = div( null, ["form-row"]);
    const labelHtml = label(labelText, ["form-label"]);
    labelHtml.style.width = "140px"; // Set a fixed width for the labels
    labelHtml.style.marginTop = "10px";
    const inputHtml = input(null, [inputClassName]);
    inputHtml.name = inputName; // Set the name attribute of the input element
    inputs[inputName] = inputHtml; // Store the input element reference in the object
    row.appendChild(labelHtml);
    row.appendChild(inputHtml);
    return row;
}

export default {
    logout,
    getUser,
    getSignup,
    getLogin
}
