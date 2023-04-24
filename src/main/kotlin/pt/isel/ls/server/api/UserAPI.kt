package pt.isel.ls.server.api

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.OK
import pt.isel.ls.server.annotations.Auth
import pt.isel.ls.server.services.UserServices
import pt.isel.ls.server.utils.Avatar
import pt.isel.ls.server.utils.UserIn
import pt.isel.ls.server.utils.UserOut

class UserAPI(private val services: UserServices) {

    fun createUser(request: Request): Response {
        val newUser = Json.decodeFromString<UserIn>(request.bodyString())
        val createdUser = services.createUser(newUser.name, newUser.email)
        return createRsp(CREATED, UserOut(createdUser.first, createdUser.second))
    }

    @Auth
    @Suppress("unused")
    fun getUser(request: Request, token: String): Response {
        val user = services.getUser(token)
        return createRsp(OK, user)
    }

    @Auth
    fun changeAvatar(request: Request, token: String): Response {
        val avatar = Json.decodeFromString<Avatar>(request.bodyString())
        services.changeAvatar(token, avatar.imgUrl)
        return createRsp(OK, Unit)
    }
}
