package pt.isel.ls

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.OK
import org.slf4j.LoggerFactory

val logger = LoggerFactory.getLogger("pt.isel.ls.http.HTTPServer")

fun postUser(request : Request) : Response {
    logRequest(request)
    val newUser = Json.decodeFromString<User>(request.bodyString()) // deserializes
    val createdUser = createUser(newUser.name,newUser.email)
    return Response(CREATED)
        .header("content-type","application/json")
        .body(Json.encodeToString(createdUser))
}

fun getUserDetails(request : Request) : Response{
    TODO()
}


fun logRequest(request : Request) {
    logger.info(
        "incoming request: method={}, uri={}, content-type={} accept={}",
        request.method,
        request.uri,
        request.header("content-type"),
        request.header("accept")
    )
}