package pt.isel.ls.server.api

import pt.isel.ls.server.services.Services

class WebAPI(services: Services) {
    val userAPI = UserAPI(services.userServices)
    val boardAPI = BoardAPI(services.boardServices)
    val listAPI = ListAPI(services.listServices)
    val cardAPI = CardAPI(services.cardServices)
}
