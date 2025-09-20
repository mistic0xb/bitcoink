package org.mist.ecc

import java.math.BigInteger

/*
[secp256k1]
y^2 = x^3 + ax + b
y^2 = x^3 + 7
here, a = 0, b = 7
*/
data class Point(val x: FieldElement?, val y: FieldElement?, val a: FieldElement, val b: FieldElement) {
    init {
        if (x != null && y != null) {
            if (y.pow(2.toBigInteger()) != x.pow(3.toBigInteger()) + a * x + b) {
                throw IllegalArgumentException(
                    "($x,$y) is not on the curve"
                )
            }
        }
    }

    operator fun plus(other: Point): Point {
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
        if (x != other.x) {
            val slope = (other.y!! - y!!) / (other.x - x)
            val x3 = slope.pow(2.toBigInteger()) - other.x - x
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
            val slope = (FieldElement(
                3.toBigInteger(),
                x.prime
            ) * x.pow(2.toBigInteger()) + a) / (FieldElement(2.toBigInteger(), y!!.prime) * y)
            val x3 = slope.pow(2.toBigInteger()) - FieldElement(2.toBigInteger(), x.prime) * x
            val y3 = slope * (x - x3) - y
            return Point(
                x = x3,
                y = y3,
                a = a,
                b = b
            )
        }

        // P1 == P2 && y == 0
        if (this == other && y?.num == BigInteger.ZERO) {
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

    operator fun times(coefficient: BigInteger): Point {
        var coef = coefficient
        var curr = this
        var res = Point(null, null, a, b)
        while (coef > BigInteger.ZERO) {
            if ((coef and BigInteger.ONE) == BigInteger.ONE) {
                res += curr
            }
            curr += curr
            coef = coef shr 1
        }
        return res
    }
}