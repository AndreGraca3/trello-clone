import {mainContent} from "../../config/storage.js"

export function createElement(tagName, innerText, classArr, id, ...children) {
    const element = document.createElement(tagName);

    if (classArr !== null) classArr.forEach(it => element.classList.add(it))
    if (id != null) element.id = id
    if (innerText != null) element.innerText = innerText

    children.forEach(child => {
        if (typeof child === "string") {
            const textNode = document.createTextNode(child)
            element.appendChild(textNode)
        } else {
            element.appendChild(child)
        }
    })

    mainContent.appendChild(element)
    return element
}

export function div(innerText, classArr, id, ...children) {
    return createElement("div", innerText, classArr, id, children)
}

export function button(innerText, classArr, id, ...children) {
    return createElement("button", innerText, classArr, id, children)
}

export function h1(innerText, classArr, id, ...children) {
    return createElement("h1", innerText, classArr, id, children)
}

export function h5(innerText, classArr, id, ...children) {
    return createElement("h5", innerText, classArr, id, children)
}

export function p1(innerText, classArr, id, ...children) {
    return createElement("p1", innerText, classArr, id, children)
}

export function p2(innerText, classArr, id, ...children) {
    return createElement("p2", innerText, classArr, id, children)
}

export function span(innerText, classArr, id, ...children) {
    return createElement("span", innerText, classArr, id, children)
}

export function input(innerText, classArr, id, ...children) {
    return createElement("span", innerText, classArr, id, children)
}

export function option(innerText, classArr, id, ...children) {
    return createElement("option", innerText, classArr, id, children)
}

export function select(innerText, classArr, id, ...children) {
    return createElement("select", innerText, classArr, id, children)
}

export function img(innerText, classArr, id, ...children) {
    return createElement("img", innerText, classArr, id, children)
}

export function p(innerText, classArr, id, ...children) {
    return createElement("p", innerText, classArr, id, children)
}

export function form(innerText, classArr, id, ...children) {
    return createElement("form", innerText, classArr, id, children)
}

export function label(innerText, classArr, id, ...children) {
    return createElement("label", innerText, classArr, id, children)
}

export function ul(innerText, classArr, id, ...children) {
    return createElement("ul", innerText, classArr, id, children)
}

export function li(innerText, classArr, id, ...children) {
    return createElement("li", innerText, classArr, id, children)
}

export function textarea(innerText, classArr, id, ...children) {
    return createElement("textarea", innerText, classArr, id, children)
}

export function a(innerText, classArr, id, ...children) {
    return createElement("a", innerText, classArr, id, children)
}

export function nav(innerText, classArr, id, ...children) {
    return createElement("nav", innerText, classArr, id, children)
}