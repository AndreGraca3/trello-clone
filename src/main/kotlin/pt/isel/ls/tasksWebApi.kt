package pt.isel.ls

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.path
import org.slf4j.LoggerFactory

val logger = LoggerFactory.getLogger("pt.isel.ls.http.HTTPServer")

@Serializable
data class UserIn(val name: String, val email: String)

class WebApi(private val services: Services) {
    fun postUser(request: Request): Response {
        return try {
            logRequest(request)
            val newUser = Json.decodeFromString<UserIn>(request.bodyString()) // deserializes
            val createdUser = services.createUser(newUser.name, newUser.email)
            Response(OK)
                .header("content-type","application/json")
                .body(Json.encodeToString(createdUser))
        } catch(e : Exception) {
            Response(BAD_REQUEST)
                .header("content-type","application/json")
                .body(Json.encodeToString(e.message))
        }
    }

    fun getUserDetails(request: Request): Response {
        logRequest(request)
        val userId = request.path("idUser")?.toIntOrNull()
        return if(userId == null) {
            Response(BAD_REQUEST)
                .header("content-type","application/json")
                .body(Json.encodeToString("Missing parameters!"))
        } else {
            Response(OK)
                .header("content-type","application/json")
                .body(Json.encodeToString(services.getUserInfo(userId)))
        }
    }
}


fun logRequest(request: Request) {
    logger.info(
        "incoming request: method={}, uri={}, content-type={} accept={}",
        request.method,
        request.uri,
        request.header("content-type"),
        request.header("accept")
    )
}