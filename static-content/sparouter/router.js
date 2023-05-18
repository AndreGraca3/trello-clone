const routes = []
let notFoundRouteHandler = () => { throw "Route handler for unknown routes not defined" }

function addRouteHandler(path, handler) {
    routes.push({path,handler})
}

function getRouteHandler(path) {
    const route = routes.find(r => r.path === path )
    return route ? route.handler : notFoundRouteHandler
}

const router = {
    addRouteHandler,
    getRouteHandler
}

export default router