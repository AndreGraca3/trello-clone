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
    router.addRouteHandler("board/:idBoard/list/:idList/card/:idCard", handlers.getCard)
    router.addRouteHandler("board/:idBoard", handlers.getBoard)
    router.addRouteHandler("board/:idBoard/lists", handlers.getLists)
    router.addRouteHandler("board/:idBoard/list", handlers.createListForm)

    hashChangeHandler()
}

function hashChangeHandler() {
    const mainContent = document.getElementById("mainContent")
    let path = window.location.hash.replace("#", "")

    let obj = handlePath(path)
    const handler = router.getRouteHandler(obj.path) // ,obj.args

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

//board/123/list/456/card/789
//board/:id/list/:id/card/:id
function handlePath(path) {
    let res = { path : "" , args : [] }

    if(checkPaging(path)){
        const start = path.indexOf("?")

        const queryString = path.substring(start) // "?limit=20&skip=20"

        const queryStringParams = queryString.replace("?","").split("&") // ["limit=20","skip=20"]

        for (let i = 0; i < queryStringParams.length; i++) {
            const parameter = queryStringParams[i].split("=") // ["limit","20"]

            res.args[parameter[0]] = parseInt(parameter[1]) // { "limit" : 20 }
        }

        path = path.replace(queryString,"")
        console.log(path) /** debug purposes. **/
    }

    const fixedPaths = ["home","user","login","signup","boards"]

    if(fixedPaths.find(it => it == path)) {
        res.path = path
        console.log(res) /** debug purposes. **/
        return res
    }

    const biggestPath = "board/:idBoard/list/:idList/card/:idCard".split("/") // ["board",":idBoard","list",...]
    const splitPath = path.split("/")

    for (let i = 0; i < splitPath.length; i++) {

        if(splitPath[i] == biggestPath[i]) {
            res.path += biggestPath[i]
        }

        if(biggestPath[i].includes(":")) {
            res.path += biggestPath[i]
            res.args[biggestPath[i].replace(":","")] = splitPath[i]
        }

        if(i + 1 == splitPath.length) {
            res.path += splitPath[i]
        }

        if(i + 1 < splitPath.length) { // puts "/" in path, excluding last place.
            res.path += "/"
        }
    }
    console.log(res) /** debug purposes. **/
    return res
}

function checkPaging(path) {
    return (path.includes("?") && ( path.includes("limit") || path.includes("skip") ))
}
