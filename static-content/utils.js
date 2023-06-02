import {BASE_URL} from "./config.js";
import {FetchError} from "./errors/errors.js";

/**
 * Makes an HTTP request to the specified path with the given method, body, and authentication token.
 *
 * @param {string} path - The path of the API endpoint.
 * @param {string} method - The HTTP method for the request (e.g., "GET", "POST", "PUT", "DELETE").
 * @param {object} body - The request body to send (optional).
 * @param {string} token - The authentication token (optional).
 * @returns {Promise<any>} - A promise that resolves to the parsed JSON response.
 * @throws {any} - Throws an error if the response is not successful.
 */
export async function fetchReq(path, method = "GET", body = undefined) {
    const headers = {
        "Content-Type": "application/json",
    }

    const token = sessionStorage.getItem("token")
    if (token) headers.Authorization = `Bearer ${token}`

    const options = {
        method,
        headers,
    }

    if (body) options.body = JSON.stringify(body)

    const rsp = await fetch(BASE_URL + path, options)
    const content = await rsp.json()

    if (!rsp.ok) throw new FetchError(content)

    return content
}
