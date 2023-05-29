import {fetchReq} from "../utils/utils.js";
import {BASE_URL} from "../config/storage.js";

async function createUser(name, email, password, urlAvatar) {
    return await fetchReq(
        "user",
        "POST",
        {
            name: name,
            email: email,
            password: password,
            urlAvatar: urlAvatar
        }
    )
}

async function getUser() {
    return await fetchReq("user", "GET");
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

export async function getUserAvatar(token) {
    if(token == null) return 'https://i.imgur.com/JGtwTBw.png'
    const user = await getUser()
    return `${user.avatar ?? 'https://i.imgur.com/JGtwTBw.png'}`
}

export default {
    createUser,
    getUser,
    login,
    changeAvatar,
    getUserAvatar
}