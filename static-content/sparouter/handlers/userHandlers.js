import {fetchReq} from "../utils/auxs/utils.js"
import {changeUserAvatar, createUser, loginUser} from "../utils/listenerHandlers/userFuncs.js";
import {BASE_URL, mainContent, user} from "../utils/storage.js";
import {createElement} from "../utils/components/components.js";
import {getUserAvatar} from "../utils/auxs/modelAuxs.js";


async function getUser(args, token) {

    document.title = "OurTrello | User";

    const user = await fetchReq("user", "GET");

    const img = createElement("img", null, "avatar")
    img.classList.add("avatarImg")
    //img.src = "https://i.imgur.com/6VBx3io.png"; //TODO: change to user avatar
    img.src = user.avatar
    img.addEventListener("click", async () => {
        await changeUserAvatar(sessionStorage.getItem("token"));
    });

    createElement("div", null, "text-center", null,
        img,
        createElement("p", `${user.name}`),
        createElement("p", `${user.email}`)
    )
}

async function getSignup() {
    document.title = "OurTrello | Signup";

    createElement("h1", "Sign Up in OurTrello", "text-center");

    const formContainer = createElement("div", null, "form-container");

    const form = createElement("form", null, "signup-form");

    const inputs = {};

    const nameRow = createRow("Name:", "form-input", "nameInput", inputs);
    form.appendChild(nameRow);

    const emailRow = createRow("Email:", "form-input", "emailInput", inputs);
    form.appendChild(emailRow);

    const passwordRow = createRow("Password:", "form-input", "passwordInput", inputs);
    passwordRow.querySelector("input").type = "password";
    const confirmPasswordRow = createRow("Confirm Password:", "form-input", "confirmPasswordInput", inputs);
    confirmPasswordRow.querySelector("input").type = "password";
    form.appendChild(passwordRow);
    form.appendChild(confirmPasswordRow);

    const logo = createElement("img", null, "avatar");
    logo.classList.add("avatarImg");
    logo.src = "https://i.imgur.com/6VBx3io.png"; //TODO: change to user avatar
    logo.style.height = "150px";
    logo.style.width = "150px"

    logo.addEventListener("click", async () => {
        await changeUserAvatar();
    });
    form.appendChild(logo);

    const submit = createElement("button", "Submit", "btn-secondary");
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

    formContainer.appendChild(form);
    formContainer.appendChild(submit);
    mainContent.appendChild(formContainer);
}

async function getLogin() {
    document.title = "OurTrello | Login";

    createElement("h1", "Log In to OurTrello", "text-center");

    const formContainer = createElement("div", null, "form-container");

    const form = createElement("form", null, "login-form");

    const inputs = {};

    const emailRow = createRow("Email:", "form-input", "emailInput", inputs);
    form.appendChild(emailRow);

    const passwordRow = createRow("Password:", "form-input", "passwordInput", inputs);
    passwordRow.querySelector("input").type = "password";
    form.appendChild(passwordRow);

    const submit = createElement("button", "Log In", "btn-primary");
    submit.style.marginTop = "20px"

    submit.addEventListener("click", async () => {
        const email = inputs["emailInput"].value;
        const password = inputs["passwordInput"].value;

        await loginUser(email, password);
    });

    formContainer.appendChild(form);
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
    const row = createElement("div", null, "form-row");
    const label = createElement("label", labelText, "form-label");
    label.style.width = "140px"; // Set a fixed width for the labels
    label.style.marginTop = "10px";
    const input = createElement("input", null, inputClassName);
    input.name = inputName; // Set the name attribute of the input element
    inputs[inputName] = input; // Store the input element reference in the object
    row.appendChild(label);
    row.appendChild(input);
    return row;
}

export default {
    logout,
    getUser,
    getSignup,
    getLogin
}
