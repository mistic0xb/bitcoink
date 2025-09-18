package org.mist

import org.mist.ecc.Point

fun main() {
    val p1 = Point(2.0, 5.0, 5.0, 7.0)
    val p2 = Point(-1.0, -1.0, 5.0, 7.0)
    println(p1.add(p2))
}