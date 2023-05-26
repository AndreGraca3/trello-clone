import {fetchReq} from "../auxs/utils.js";


export async function changeUserAvatar() {
    const input = document.createElement('input');
    input.type = 'file';
    input.accept = 'image/*';

    input.addEventListener('change', () => {
        const file = input.files[0]
        if (!file) return

        const reader = new FileReader()
        reader.onload = async () => {
            const imgUrl = reader.result
            if(sessionStorage.getItem("token") !== "null") {
                await fetchReq("user/avatar", "PUT", {imgUrl})
                document.querySelectorAll('.avatarImg').forEach(a => a.src = imgUrl)
            }
            document.querySelector('.avatar').src = imgUrl
        }
        reader.readAsDataURL(file)
    })

    input.click()
}

export async function createUser(name, email, password, urlAvatar) {
    const res = await fetchReq(
        "user",
        "POST",
        {
            name: name,
            email: email,
            password: password,
            urlAvatar: urlAvatar
        }
    )
    if (res !== null) {
        sessionStorage.setItem("token", res.token)
        //console.log(res);
        //console.log(sessionStorage.getItem("token").toString())
        document.querySelector('#user-option').style.display = "block"
        document.querySelector('#logout-option').style.display = "block"
        document.location = "#user";
    }
    else {
        document.querySelector('.toast-body').innerText = "Error creating user!"
        $('.toast').toast('show')
    }

}

export async function loginUser(email, password) {
    const res = await fetchReq(
        "user/login",
        "POST",
        {
            email: email,
            password: password
        }
    )
    if(res !== null) {
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


