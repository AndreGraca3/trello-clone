import router from "./router.js"
import handlers from "./handlers.js"

window.addEventListener('load',loadHandler)
window.addEventListener('hashchange',hashChangeHandler)

function loadHandler() {
    router.addRouteHandler("home",handlers.getHome)
    router.addRouteHandler("user",handlers.getUser)
    router.addRouteHandler("login",handlers.getLogin)
    router.addRouteHandler("signup",handlers.getSignup)

    hashChangeHandler()
}

function hashChangeHandler() {
    const mainContent = document.getElementById("mainContent")
    const path = window.location.hash.replace("#","")

    const handler = router.getRouteHandler(path)

    if (path === "user") {
        const token = localStorage.getItem('token')
        if (token) {
            handler(mainContent, token)
        } else {
            window.location.hash = "login"
        }
    } else {
        handler(mainContent)
    }
}
