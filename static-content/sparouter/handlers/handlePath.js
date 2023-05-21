//board/123/list/456/card/789
//board/:id/list/:id/card/:id

const fixedPaths = ["home", "user", "login", "signup", "boards"]
const biggestPath = "board/:idBoard/list/:idList/card/:idCard".split("/")
export function handlePath(path) {
    let res = {path: "", args: []}

    if (checkQuery(path)) {
        path = getQueryParams(path, res)
    }

    if (fixedPaths.find(it => it === path)) {
        res.path = path
        console.log(res) /** debug purposes. **/
        return res
    }

    const splitPath = path.split("/")

    for (let i = 0; i < splitPath.length; i++) {

        if (splitPath[i] === biggestPath[i]) {
            res.path += biggestPath[i]
        }

        if (biggestPath[i].includes(":")) {
            res.path += biggestPath[i]
            res.args[biggestPath[i].replace(":", "")] = splitPath[i]
        }

        if (i + 1 === splitPath.length && !biggestPath[i].includes(":")) {
            res.path += splitPath[i]
        }

        if (i + 1 < splitPath.length) { // puts "/" in path, excluding last place.
            res.path += "/"
        }
    }
    console.log(res) /** debug purposes. **/
    return res
}

function checkQuery(path) {
    return (path.includes("?") &&
        (
            path.includes("limit")
            || path.includes("skip")
            || path.includes("action")
            || path.includes("name")
            || path.includes("numLists")
        )
    )
}

function getQueryParams(path, res) {
    const start = path.indexOf("?")

    const queryString = path.substring(start) // "?limit=20&skip=20&action=delete"

    const queryStringParams = queryString.replace("?", "").split("&") // ["limit=20","skip=20","action=delete"]

    console.log(queryStringParams)
    for (let i = 0; i < queryStringParams.length; i++) {

        const parameter = queryStringParams[i].split("=") // ["limit","20"]

        res.args[parameter[0]] = parameter[1] // { "limit" : "20" }
    }

    return path.replace(queryString, "")
}

export function addOrChangeQuery(query, value) {
    const hash = document.location.hash.split('?')
    const params = new URLSearchParams(hash[1])
    params.set(query, value)
    document.location.hash = hash[0] + '?' + params.toString()
}
