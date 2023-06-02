import {changeUserAvatar, createUser, loginUser} from "../../html/dsl/listeners/userFuncs.js";
import {button, div, form, h1, img, input, label, p, small} from "../../html/components/elements.js";
import userData from "../../data/userData.js";

async function getUser() {

    const user = await userData.getUser()

    const imgHtml = img(null, ["avatar", "avatarImg"])
    imgHtml.src = await user.avatar
    imgHtml.addEventListener("click", async () => {
        await changeUserAvatar()
    })

    div(null, ["text-center"], null,
        imgHtml,
        p(`${user.name}`),
        p(`${user.email}`)
    )
}

async function getSignup() {

    const image = img(null, ["logo-title"])
    image.src = "../resources/images/ourTrello-logo-title.png"
    const title = h1("Sign Up", ["text-center", "sign-title"])

    const email = input(null, ["form-control"], "exampleInputEmail1")
    email.type = "email"
    email.placeholder = "Enter email"

    const password = input(null, ["form-control"], "exampleInputPassword1")
    password.type = "password"
    password.placeholder = "Enter password"

    const name = input(null, ["form-control"])
    name.placeholder = "Enter name"

    const mailContainer = div(null, ["form-group", "sign-prop-container"], null,
        label("Email address", ["email-label"]),
        email,
    )

    const passContainer = div(null, ["form-group", "sign-prop-container"], null,
        label("Password", ["password-label"]),
        password,
        small("We'll never share your email with anyone else.", ["form-text", "text-muted"], "emailHelp")
    )

    const nameContainer = div(null, ["form-group", "sign-prop-container"], null,
        label("Name", ["name-label"]),
        name,
    )

    const submit = button("Sign Up", ["btn", "btn-primary", "submit-signup"])

    submit.onclick = async () => {
        await createUser(name.value, email.value, password.value, null)
    }

    div(null, ["signUp-container"], null, image, title,
        form(null, ["sign-form-container"], null, nameContainer, mailContainer, passContainer, submit)
    )
}

async function getLogin() {

    const image = img(null, ["logo-title"])
    image.src = "../resources/images/ourTrello-logo-title.png"
    const title = h1("Log In", ["text-center", "sign-title"])

    const email = input(null, ["form-control"], "exampleInputEmail1")
    email.type = "email"
    email.placeholder = "Enter email"

    const password = input(null, ["form-control"], "exampleInputPassword1")
    password.type = "password"
    password.placeholder = "Enter password"

    const mailContainer = div(null, ["form-group", "sign-prop-container"], null,
        label("Email address", ["email-label"]),
        email,
    )

    const passContainer = div(null, ["form-group", "sign-prop-container"], null,
        label("Password", ["password-label"]),
        password,
    )

    const submit = button("Login", ["btn", "btn-primary", "submit-signup"])

    submit.onclick = async (e) => {
        e.preventDefault()
        await loginUser(email.value, password.value)
    }

    div(null, ["signUp-container"], null, image, title,
        form(null, ["sign-form-container"], null, mailContainer, passContainer, submit)
    )
}

export default {
    getUser,
    getSignup,
    getLogin
}
