import {BASE_URL, user} from "../storage.js";

export function randomColor() {
    const rgb = Array.from({length: 3}, () => Math.floor(Math.random() * 256));
    return `rgb(${rgb.join(', ')})`;
}

export function darkerColor(color) {
    const [r, g, b] = color.slice(4, -1).split(", ");
    const newR = Math.max(Number(r) - 100, 0);
    const newG = Math.max(Number(g) - 100, 0);
    const newB = Math.max(Number(b) - 100, 0);
    return `rgb(${newR}, ${newG}, ${newB})`;
}

export async function fetchReq(path, method, body) {
    const options = {
        method,
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${user.token}`
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

export function getNewBoardsPath(skip, limit, nameSearch, numLists) {
    return `#boards?skip=${skip}&limit=${limit}${nameSearch != null && nameSearch !== "" ? `&name=${nameSearch}` : ''}${numLists != null && numLists !== "" ? `&numLists=${numLists}` : ''}`
}

export function getLimitSelectorOptions(maxDisplay, size, limit) {
    const limitOptions = Array.from({length: size}, (_, i) => (i + 1) * maxDisplay)
    if (!limit || limitOptions.includes(limit)) return limitOptions
    limitOptions.push(limit)
    limitOptions.sort((a, b) => a - b)
    return limitOptions
}