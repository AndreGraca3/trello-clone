import {BASE_URL} from "../config/storage.js";


export async function fetchReq(path, method, body) {
    const token = sessionStorage.getItem('token')
    //const token = user.token
    const options = {
        method,
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        }
    }

    if (body !== undefined) {
        options.body = JSON.stringify(body)
    }

    const rsp = await fetch(BASE_URL + path, options)
    const content = await rsp.json()

    if (!rsp.ok) {
        console.log()
        document.querySelector('.toast-body').innerText = content
        $('.toast').toast('show')
        throw content
    }
    return content
}


