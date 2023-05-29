import {fetchReq} from "../../../utils/utils.js";
import {input} from "../../components/components.js";
import userData from "../../../data/userData.js";

export async function changeUserAvatar() {
    const inputHtml = input();
    inputHtml.type = 'file';
    inputHtml.accept = 'image/*';

    inputHtml.addEventListener('change', () => {
        const file = inputHtml.files[0]
        if (!file) return

        const reader = new FileReader()
        reader.onload = async () => {
            const imgUrl = reader.result
            if (sessionStorage.getItem("token") !== "null") {
                await userData.changeAvatar(imgUrl)
                document.querySelectorAll('.avatarImg').forEach(a => a.src = imgUrl)
            }
            document.querySelector('.avatar').src = imgUrl
        }
        reader.readAsDataURL(file)
    })

    inputHtml.click()
}

export async function createUser(name, email, password, urlAvatar) {
    const res = await userData.createUser(name, email, password, urlAvatar)

    if (res !== null) {
        sessionStorage.setItem("token", res.token)
        //console.log(res);
        //console.log(sessionStorage.getItem("token").toString())
        document.querySelector('#user-option').style.display = "block"
        document.querySelector('#logout-option').style.display = "block"
        document.location = "#user";
    } else {
        document.querySelector('.toast-body').innerText = "Error creating user!"
        $('.toast').toast('show')
    }

}

export async function loginUser(email, password) {
    const res = await userData.login(email, password)

    if (res !== null) {
        document.querySelectorAll('.avatarImg').forEach(a => a.src = res.avatar)
        sessionStorage.setItem("token", res)
        document.querySelector('#user-option').style.display = "block"
        document.querySelector('#logout-option').style.display = "block"
        //console.log(res)
        document.location = "#user";
    } else {
        document.querySelector('.toast-body').innerText = "User not found!"
        $('.toast').toast('show')
    }

}


// export async function createUser() {
//     const userName = $('#user-name').val()
//     const userEmail = $('#user-email').val()
//     const userPassword = $('#user-password').val()
//
//     const res = await fetchReq("user", "POST", {name: userName, email: userEmail, password: userPassword})
//
//     document.querySelector('.toast-body').innerText = "User Created Successfully!"
//     $('.toast').toast('show')
//
//     hideCreateUserModal()
//     document.location = `#user`
// }
//
// export function showCreateUserModal() {
//     $('#createBoardModal').modal('show')
// }
//
// export function hideCreateUserModal() {
//     $('#createBoardModal').modal('hide')
// }


