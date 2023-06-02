import router from "./router.js"
import defaultHandlers from "./site/handlers/defaultHandlers.js"
import userHandlers from "./site/handlers/userHandlers.js";
import boardHandlers from "./site/handlers/boardHandlers.js";
import {mainContent} from "./config.js";
import {handleError} from "./errors/errors.js";

window.addEventListener('load', loadHandler)
window.addEventListener('hashchange', hashChangeHandler)
window.addEventListener('unhandledrejection', e => handleError(e.reason))

async function loadHandler() {
    router.addRouteHandler("home", defaultHandlers.getHome)
    router.addRouteHandler("user", userHandlers.getUser)
    router.addRouteHandler("login", userHandlers.getLogin)
    router.addRouteHandler("signup", userHandlers.getSignup)
    router.addRouteHandler("boards", boardHandlers.getBoards)
    router.addRouteHandler("board/:idBoard", boardHandlers.getBoard)

    await hashChangeHandler()
}

async function hashChangeHandler() {
    mainContent.style.background = "rgb(42, 40, 40)"
    mainContent.replaceChildren()

    let path = window.location.hash.slice(1)

    const obj = router.getRouteHandler(path)
    document.title = `OurTrello | ${obj.handler.name.slice(3)}`

    await obj.handler(obj.args, sessionStorage.getItem("token"))
}