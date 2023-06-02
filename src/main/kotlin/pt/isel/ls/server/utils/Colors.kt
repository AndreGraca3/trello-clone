package pt.isel.ls.server.utils

import kotlin.random.Random

enum class Colors {
    RED,
    BLUE,
    GREEN,
    ORANGE,
    YELLOW,
    PURPLE,
    PINK,
    CYAN,
    GRAY,
    BLACK,
    SILVER,
    NAVY,
    TEAL,
    LIME
}

fun randomColor() = Colors.values()[Random.nextInt(Colors.values().size)].name
