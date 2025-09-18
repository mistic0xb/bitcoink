package org.mist.ecc

import kotlin.math.pow

/*
[secp256k1]
y^2 = x^3 + ax + b
y^2 = x^3 + 7
here, a = 0, b = 7
*/
data class Point(val x: Double?, val y: Double?, val a: Double, val b: Double) {
    init {
        if (x != null && y != null) {
            if (y.pow(2) != x.pow(3) + a * x + b) {
                throw IllegalArgumentException(
                    "($x,$y) is not on the curve"
                )
            }
        }
    }

    fun add(other: Point): Point {
        if (a != other.a || b != other.b) {
            throw IllegalArgumentException(
                "Points ($x,$y) are not on the same curve"
            )
        }
        // handle infinity
        if (x == null) {
            return other
        }
        if (other.x == null) {
            return this
        }

        // A + (-A) = I (tangent is vertical)
        if (x == other.x && y != other.y) {
            return Point(
                x = null,
                y = null,
                a = a,
                b = b
            )
        }

        // x1 != x2
        if (x != other.x ) {
            val slope = (other.y!! - y!!) / (other.x - x)
            val x3 = slope.pow(2) - other.x - x
            val y3 = slope * (x - x3) - y
            return Point(
                x = x3,
                y = y3,
                a = a,
                b = b
            )
        }

        // P1 == P2
        if (this == other) {
            val slope = (3 * x.pow(2) + a) / (2 * y!!)
            val x3 = slope.pow(2) - 2 * x
            val y3 = slope * (x - x3) - y
            return Point(
                x = x3,
                y = y3,
                a = a,
                b = b
            )
        }

        // P1 == P2 && y == 0
        if (this == other && y == 0.0) {
            return Point(
                x = null,
                y = null,
                a = a,
                b = b
            )
        }
        throw IllegalStateException(
            "Unhandled case in point addition for point() for:\n point($x,$y) on eq: y^2 = x^3 + ${a}x + $b"
        )
    }
}