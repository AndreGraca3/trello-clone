package pt.isel.ls.server.api

import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.OK
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import pt.isel.ls.server.utils.UserIn
import pt.isel.ls.server.utils.UserOut
import pt.isel.ls.server.annotations.Auth
import pt.isel.ls.server.services.UserServices

class UserWebApi(private val services: UserServices) {

    fun createUser(request: Request): Response {
        return handleRequest(request, ::createUserInternal)
    }

    fun getUser(request: Request): Response {
        return handleRequest(request, ::getUserInternal)
    }

    private fun createUserInternal(request: Request): Response {
        val newUser = Json.decodeFromString<UserIn>(request.bodyString())
        val createdUser = services.createUser(newUser.name, newUser.email)
        return createRsp(CREATED, UserOut(createdUser.first, createdUser.second))
    }

    @Auth
    private fun getUserInternal(request: Request, token: String): Response {
        val user = services.getUser(token)
        return createRsp(OK, user)
    }
}
