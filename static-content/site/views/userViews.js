import {button, div, fileInput, form, h1, img, input, label, p, small} from "../../html/common/components/elements.js";
import {changeUserAvatar, createUser, loginUser} from "../../html/dsl/listeners/userFuncs.js";
import userData from "../../data/userData.js";

function accountPageView(user) {
    const imgHtml = img(null, ["avatar", "avatarImg"])
    imgHtml.src = user.avatar
    imgHtml.addEventListener("click", async () => {
        await changeUserAvatar()
    })

    div(null, ["text-center"], null,
        imgHtml,
        p(`${user.name}`),
        p(`${user.email}`)
    )
}

function signUpLoginPageView(isSignUp) {
    const image = img(null, ["logo-title"])
    image.src = "../resources/images/ourTrello-logo-title.png"

    const title = h1(isSignUp ? "Sign Up" : "Log In", ["text-center", "sign-title"])

    const email = input(null, ["form-control"], "exampleInputEmail1", "Enter email")
    email.type = "email"

    const password = input(null, ["form-control"], "exampleInputPassword1", "Enter password")
    password.type = "password"

    const mailContainer = div(null, ["form-group", "sign-prop-container"], null,
        label("Email address", ["email-label"]),
        email,
    )

    const passContainer = div(null, ["form-group", "sign-prop-container"], null,
        label("Password", ["password-label"]),
        password,
        isSignUp ? small("We'll never share your email with anyone else.", ["form-text", "text-muted"],
            "emailHelp") : null
    )

    const avatar = img(null, ["avatar-icon"])
    const token = sessionStorage.getItem("token")
    userData.getUserAvatar(token).then((a) => (avatar.src = a))
    if (isSignUp) {
        avatar.classList.add("clickable")
        const handler = (reader) => {
            avatar.src = reader.result
        }
        avatar.addEventListener("click", async () => fileInput(handler))
    }
    const avatarContainer = div(null, ["avatar-signUp"], null, avatar)

    const name = isSignUp ? input(null, ["form-control"], "nameInput", "Enter name") : null

    const nameContainer = isSignUp ? div(null, ["form-group", "sign-prop-container"], null,
        label("Name", ["name-label"]),
        name
    ) : null

    const submit = button(isSignUp ? "Sign Up" : "Login", ["btn", "btn-primary", "submit-signup"])

    submit.onclick = async (e) => {
        e.preventDefault()
        if (isSignUp) {
            await createUser(name.value, email.value, password.value, avatar.src)
        } else {
            await loginUser(email.value, password.value);
        }
    }

    const formContainer =
        form(null, ["sign-form-container"], null,
            avatarContainer,
            nameContainer,
            mailContainer,
            passContainer,
            submit
        )

    div(null, ["signUp-container"], null, image, title, formContainer)
}


export default {
    accountPageView,
    signUpLoginPageView
}