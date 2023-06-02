import {fetchReq} from "../utils.js";
import {BASE_URL} from "../config.js";

async function createUser(name, email, password, urlAvatar) {
    return await fetchReq(
        "user",
        "POST",
        {
            name: name,
            email: email,
            password: password,
            avatar: urlAvatar
        }
    )
}

async function getUser(token) {
    return await fetchReq("user", "GET", null)
}

async function getAllUsers(idBoard) {
    return await fetchReq(`board/${idBoard}/allUsers`, "GET")
}

async function login(email, password) {
    return await fetchReq(
        "user/login",
        "POST",
        {
            email: email,
            password: password
        }
    )
}

async function changeAvatar(imgUrl) {
    return await fetchReq("user/avatar", "PUT", {imgUrl})
}

async function getUserAvatar() {
    const token = sessionStorage.getItem("token")
    const noUserIcon = "../resources/images/user-unknown-icon.png"
    if(!token) return noUserIcon
    const user = await getUser(token)
    return `${user.avatar === "null" ? noUserIcon : user.avatar}`
}

export default {
    createUser,
    getUser,
    getAllUsers,
    login,
    changeAvatar,
    getUserAvatar
}