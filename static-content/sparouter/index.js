import router from "./router.js"
import handlers from "./handlers.js"

window.addEventListener('load',loadHandler)
window.addEventListener('hashchange',hashChangeHandler)

function loadHandler() {
    router.addRouteHandler("home",handlers.getHome)
    router.addRouteHandler("user",handlers.getUser)

    hashChangeHandler()
}

function hashChangeHandler() {

    const mainContent = document.getElementById("mainContent")
    const path = window.location.hash.replace("#","")

    const handler = router.getRouteHandler(path)

    handler(mainContent)
}