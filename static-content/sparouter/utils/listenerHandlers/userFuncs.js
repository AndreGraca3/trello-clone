import {fetchReq} from "../auxs/utils.js";
import {user} from "../storage.js";


export async function changeUserAvatar() {
    const input = document.createElement('input');
    input.type = 'file';
    input.accept = 'image/*';

    input.addEventListener('change', () => {
        const file = input.files[0]
        if (!file) return

        const reader = new FileReader()
        reader.onload = () => {
            const imgUrl = reader.result
            fetchReq("user/avatar", "PUT", {imgUrl})
            user.avatar = imgUrl
            document.location.reload()
        }
        reader.readAsDataURL(file)
    })

    input.click()
}
