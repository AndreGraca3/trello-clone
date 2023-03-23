package pt.isel.ls.server.web.api

import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import pt.isel.ls.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import pt.isel.ls.server.annotations.Auth
import pt.isel.ls.server.createRsp
import pt.isel.ls.server.services.UserServices

class UserAPI(private val services: UserServices) {

    fun createUser(request: Request): Response {
        return handleRequest(request, ::createUserInternal)
    }

    fun getUser(request: Request): Response {
        return handleRequest(request, ::getUserInternal)
    }


    fun createUserInternal(@Auth request: Request, token: String): Response {
        val newUser = Json.decodeFromString<UserIn>(request.bodyString())
        val createdUser = services.createUser(newUser.name, newUser.email)
        return createRsp(OK, UserOut(createdUser.first, createdUser.second))
    }

    fun getUserInternal(request: Request, token: String): Response {
        val user = services.getUser(token)
        return createRsp(OK, user)
    }
}