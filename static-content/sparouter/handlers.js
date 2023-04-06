
const BASE_URL = "http://localhost:8080/"

function getHome(mainContent) {
    const h1 = document.createElement("h1")
    const text = document.createTextNode("home Page")
    h1.replaceChildren(text)
    mainContent.replaceChildren(h1)
}

function getUser(mainContent) {
    fetch(BASE_URL + "user")
        .then(res => res.json())
        .then(user => {
            console.log(user)
            const text = document.createTextNode(JSON.stringify(user));
            mainContent.replaceChildren(text)
        })
}

export const handlers = {
    getHome,
    getUser
}

export default handlers