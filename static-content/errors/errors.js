import defaultHandlers from "../site/handlers/defaultHandlers.js";


export class FetchError extends Error {
    constructor(message) {
        super(message)
        this.name = 'FetchError'
    }
}

export function handleError(e) {
    if(e instanceof FetchError) {
        document.querySelector('.toast-body').innerText = e.message
        $('.toast').toast('show')
    } else {
        defaultHandlers.getErrorPage(e)
    }
}