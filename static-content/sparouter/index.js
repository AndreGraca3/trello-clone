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
    router.addRouteHandler("board/:idBoard/card/:idCard", handlers.getCard)
    router.addRouteHandler("board/:idBoard", handlers.getBoard)
    router.addRouteHandler("user/avatar", handlers.changeUserAvatar)

    await hashChangeHandler()
}

async function hashChangeHandler() {
    const mainContent = document.getElementById("mainContent")
    mainContent.style.background = "rgb(42, 40, 40)"

    let path = window.location.hash.replace("#", "")

    let obj = handlePath(path)
    const handler = router.getRouteHandler(obj.path) // ,obj.args

    try {
        await handler(mainContent, obj.args, user.token)
    } catch (e) {
        handlers.getErrorPage(mainContent, e)
        throw e
    }
}

//board/123/list/456/card/789
//board/:id/list/:id/card/:id
function handlePath(path) {
    let res = {path: "", args: []}

    if (checkPaging(path)) {
        const start = path.indexOf("?")

        const queryString = path.substring(start) // "?limit=20&skip=20"

        const queryStringParams = queryString.replace("?", "").split("&") // ["limit=20","skip=20"]

        for (let i = 0; i < queryStringParams.length; i++) {
            const parameter = queryStringParams[i].split("=") // ["limit","20"]

            res.args[parameter[0]] = parseInt(parameter[1]) // { "limit" : 20 }
        }

        path = path.replace(queryString, "")
        console.log(path) /** debug purposes. **/
    }

    const fixedPaths = ["home", "user", "login", "signup", "boards"]

    if (fixedPaths.find(it => it === path)) {
        res.path = path
        console.log(res) /** debug purposes. **/
        return res
    }

    const biggestPath1 = "board/:idBoard/list/:idList".split("/") // ["board",":idBoard","list",...]
    const biggestPath2 = "board/:idBoard/card/:idCard".split("/") // ["board",":idBoard","card",...]
    const splitPath = path.split("/")

    for (let i = 0; i < splitPath.length; i++) {

        if (splitPath[i] === biggestPath1[i]) {
            res.path += biggestPath1[i]
        }

        if (biggestPath1[i].includes(":")) {
            res.path += biggestPath1[i]
            res.args[biggestPath1[i].replace(":", "")] = splitPath[i]
        }

        if (i + 1 === splitPath.length && !biggestPath1[i].includes(":")) {
            res.path += splitPath[i]
        }

        if (i + 1 < splitPath.length) { // puts "/" in path, excluding last place.
            res.path += "/"
        }
    }

    if (res.path !== "board/:idBoard/list/:idList" && res.path !== "board/:idBoard") {
        res.path = res.path + "/"
        for (let i = 2; i < splitPath.length; i++) {

            if (splitPath[i] === biggestPath2[i]) {
                res.path += biggestPath2[i]
            }

            if (biggestPath2[i].includes(":")) {
                res.path += biggestPath2[i]
                res.args[biggestPath2[i].replace(":", "")] = splitPath[i]
            }

            if (i + 1 === splitPath.length && !biggestPath2[i].includes(":")) {
                res.path += splitPath[i]
            }

            if (i + 1 < splitPath.length) { // puts "/" in path, excluding last place.
                res.path += "/"
            }
        }
    }

    console.log(res) /** debug purposes. **/
    return res
}

function checkPaging(path) {
    return (path.includes("?") && (path.includes("limit") || path.includes("skip")))
}
