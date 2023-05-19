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
            await fetchReq("user/avatar", "PUT", {imgUrl})
            document.querySelectorAll('.avatarImg').forEach(a => a.src = imgUrl)
        }
        reader.readAsDataURL(file)
    })

    input.click()
}
