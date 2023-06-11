package pt.isel.ls.server.utils

import org.slf4j.LoggerFactory
import java.time.LocalDate

val logger = LoggerFactory.getLogger("pt.isel.ls.http.HTTPServer")

/**
 * checks if endDate is in the future
 * @param endDate - the date to verify
 * @return true if it checks, false otherwise
 */
fun checkEndDate(endDate: String): Boolean {
    return LocalDate.parse(endDate) > LocalDate.now()
}

/**
 * checks if string isn't null or empty
 * @param value - string to verify
 * @param property - string's property name. This serves for the Exception msg
 * @throws IllegalArgumentException if string isn't valid
 */
fun validateString(value: String, property: String = "String") {
    val trim = value.trim()
    if (trim == "" || trim == "null") throw IllegalArgumentException("$property is not a valid.")
}
