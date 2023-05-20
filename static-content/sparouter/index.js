import router from "./router.js"
import defaultHandlers from "./handlers/defaultHandlers.js"
import userHandlers from "./handlers/userHandlers.js";
import boardHandlers from "./handlers/boardHandlers.js";
import {mainContent, user} from "./utils/storage.js";
import {handlePath} from "./handlers/handlePath.js";

window.addEventListener('load', loadHandler)
window.addEventListener('hashchange', hashChangeHandler)

async function loadHandler() {
    router.addRouteHandler("error", defaultHandlers.getErrorPage)
    router.addRouteHandler("home", defaultHandlers.getHome)
    router.addRouteHandler("user", userHandlers.getUser)
    router.addRouteHandler("login", userHandlers.getLogin)
    router.addRouteHandler("signup", userHandlers.getSignup)
    router.addRouteHandler("boards", boardHandlers.getBoards)
    router.addRouteHandler("board/:idBoard", boardHandlers.getBoard)
    router.addRouteHandler("user/avatar", userHandlers.changeUserAvatar)

    await hashChangeHandler()
}

async function hashChangeHandler() {
    mainContent.style.background = "rgb(42, 40, 40)"
    mainContent.replaceChildren()

    let path = window.location.hash.slice(1)

    let obj = handlePath(path)
    const handler = router.getRouteHandler(obj.path) // ,obj.args

    try {
        await handler(obj.args, user.token)
    } catch (e) {
        defaultHandlers.getErrorPage(e)
        throw e
    }
}