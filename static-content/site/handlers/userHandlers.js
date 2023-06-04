import userData from "../../data/userData.js";
import userViews from "../views/userViews.js";

async function getUser() {
    const user = await userData.getUser()

    userViews.accountPageView(user)
}

function getSignup() {
    userViews.signUpLoginPageView(true)
}

function getLogin() {
    userViews.signUpLoginPageView(false)
}

export default {
    getUser,
    getSignup,
    getLogin
}
