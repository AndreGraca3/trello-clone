package pt.isel.ls.server.api

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import pt.isel.ls.server.annotations.Auth
import pt.isel.ls.server.exceptions.TrelloException
import pt.isel.ls.server.utils.logger
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KFunction
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible


fun getToken(request: Request): String {
    val authHeader = request.header("Authorization") ?: throw TrelloException.NotAuthorized()
    return authHeader.removePrefix("Bearer ")
}

fun handleRequest(request: Request, handler: KFunction<Response>): Response {
    logRequest(request)
    handler.isAccessible = true
    return try {
        if (isAuthRequired(handler)) {
            handler.call(request, getToken(request))
        } else {
            handler.call(request)
        }
    } catch (e: Exception) {
        when (val cause = if (e is InvocationTargetException) e.targetException else e) {
            is TrelloException -> createRsp(cause.status, cause.message)
            else -> createRsp(Status.BAD_REQUEST, cause.message)
        }
    }
}

private fun isAuthRequired(function: KFunction<*>): Boolean {
    return function.hasAnnotation<Auth>()
}

inline fun <reified T> createRsp(status: Status, body: T): Response {
    return Response(status)
        .header("content-type", "application/json")
        .body(Json.encodeToString(body))
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
