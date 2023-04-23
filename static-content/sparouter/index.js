import router from "./router.js"
import handlers from "./handlers.js"
import {user} from "./utils/storage.js";

window.addEventListener('load', loadHandler)
window.addEventListener('hashchange', hashChangeHandler)

async function loadHandler() {
    router.addRouteHandler("error", handlers.getErrorPage)
    router.addRouteHandler("home", handlers.getHome)
    router.addRouteHandler("user", handlers.getUser)
    router.addRouteHandler("login", handlers.getLogin)
    router.addRouteHandler("signup", handlers.getSignup)
    router.addRouteHandler("boards", handlers.getBoards)
    router.addRouteHandler("board/:id/list/:id/card/:id", handlers.getCard)
    router.addRouteHandler("board/:id", handlers.getBoard)

    await hashChangeHandler()
}

async function hashChangeHandler() {
    const mainContent = document.getElementById("mainContent")
    mainContent.style.background = "rgb(42, 40, 40)"

    let path = window.location.hash.replace("#", "")
    path = handlePath(path)
    const handler = router.getRouteHandler(path)

    try {
        await handler(mainContent, user.token)
    } catch (e) {
        handlers.getErrorPage(mainContent, e)
        throw e
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
