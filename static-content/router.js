const routes = []
let notFoundRouteHandler = () => {
    throw "Route handler for unknown routes not defined"
}

function addRouteHandler(pathTemplate, handler) {
    routes.push({pathTemplate, handler})
}

function getRouteHandler(path) {
    let res = {args: {}, handler: null}

    if (path.includes("?")) {
        path = getQueryParams(path, res)
    }

    const splitPath = path.split("/")

    let args = {}
    for (let i = 0; i < routes.length; i++) {
        const curr = routes[i]
        let currPathTemplate = routes[i].pathTemplate
        const currPathSplit = currPathTemplate.split("/")
        for (let j = 0; j < splitPath.length; j++) {
            if (currPathSplit[j] !== splitPath[j] && !currPathSplit[j].includes(":")) {
                args = {}
                break
            }

            if (currPathSplit[j].includes(":")) {
                args[currPathSplit[j].replace(":", "")] = splitPath[j]
            }

            if (j === splitPath.length - 1) {
                res.handler = curr.handler
                Object.assign(res.args, args)
                return res
            }
        }
    }
    return {handler: notFoundRouteHandler}
}

function getQueryParams(path, res) {
    const start = path.indexOf("?")

    const queryString = path.substring(start) // "?limit=20&skip=20&action=delete"

    const queryStringParams = queryString.replace("?", "").split("&") // ["limit=20","skip=20","action=delete"]

    for (let i = 0; i < queryStringParams.length; i++) {

        const parameter = queryStringParams[i].split("=") // ["limit","20"]

        res.args[parameter[0]] = parameter[1] // { "limit" : "20" }
    }

    return path.replace(queryString, "")
}

function addOrChangeQuery(query, value) {
    const hash = document.location.hash.split('?')
    const params = new URLSearchParams(hash[1])
    params.set(query, value)
    document.location.hash = hash[0] + '?' + params.toString()
}

export default {
    addRouteHandler,
    getRouteHandler,
    getQueryParams,
    addOrChangeQuery
}