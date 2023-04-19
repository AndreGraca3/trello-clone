import router from "./router.js"
import handlers from "./handlers.js"

window.addEventListener('load', loadHandler)
window.addEventListener('hashchange', hashChangeHandler)

function loadHandler() {
    router.addRouteHandler("home", handlers.getHome)
    router.addRouteHandler("user", handlers.getUser)
    router.addRouteHandler("login", handlers.getLogin)
    router.addRouteHandler("signup", handlers.getSignup)
    router.addRouteHandler("boards", handlers.getBoards)
    router.addRouteHandler("board/:id/list/:id/card/:id", handlers.getCard)
    router.addRouteHandler("board/:id", handlers.getBoard)
    router.addRouteHandler("board/:id/list", handlers.getLists)

    hashChangeHandler()
}

function hashChangeHandler() {
    const mainContent = document.getElementById("mainContent")
    let path = window.location.hash.replace("#", "")

    path = handlePath(path)
    const handler = router.getRouteHandler(path)

    if (path === "user") {
        //const token = localStorage.getItem('token')
        const token = "token123" /* TEMPORARY */
        if (token) {
            handler(mainContent, token)
            console.log("token is", token)
        } else {
            window.location.hash = "login"
        }
    } else {
        handler(mainContent)
    }
}

//localhost:8080/board/123/list/456/card/789
//localhost:8080/board/:id/list/:id/card/:id
function handlePath(path) {
    if (path.includes("board/") && path.includes("/list") && path.includes("/card")) return "board/:id/list/:id/card/:id"
    if (path.includes("board/") && path.includes("/list")) return "board/:id/list"
    if (path.includes("board/")) return "board/:id"
    return path
}
