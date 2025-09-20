package org.mist.ecc

import java.math.BigInteger

data class FieldElement(val num: Int, val prime: Int) {
    init {
        if (num >= prime || num < 0) {
            throw IllegalArgumentException(
                "Num $num not in field range 0 to ${prime - 1}"
            )
        }
    }

    override fun toString(): String {
        return "FieldElement_$prime($num)"
    }

    private fun mod(value: Int, modulus: Int): Int {
        val result = value % modulus
        if (result < 0) {
            return result + modulus
        } else {
            return result
        }
    }

    operator fun plus(other: FieldElement): FieldElement {
        if (prime != other.prime) {
            throw IllegalArgumentException(
                "Cannot add two numbers in different Fields"
            )
        }
        val newNum = mod((num + other.num), prime)
        return FieldElement(newNum, prime)
    }

    operator fun minus(other: FieldElement): FieldElement {
        if (prime != other.prime) {
            throw IllegalArgumentException(
                "Cannot subtract two numbers in different Fields"
            )
        }
        val res = mod((num - other.num), prime)
        return FieldElement(res, prime)
    }

    operator fun times(other: FieldElement): FieldElement {
        if (prime != other.prime) {
            throw IllegalArgumentException(
                "Cannot multiply two numbers in different Fields"
            )
        }
        val res = mod((num * other.num), prime)
        return FieldElement(res, prime)
    }

    operator fun div(other: FieldElement): FieldElement {
        if (prime != other.prime) {
            throw IllegalArgumentException(
                "Cannot divide two numbers in different Fields"
            )
        }
        val res = mod((num * (other.pow(prime - 2).num)), prime)
        return FieldElement(res, prime)
    }

    fun pow(exp: Int): FieldElement {
        val n = mod(exp, (prime - 1))
        val base = BigInteger.valueOf(num.toLong())
        val mod = BigInteger.valueOf(prime.toLong())
        val positiveExpo = BigInteger.valueOf(n.toLong())
        val res = (base.modPow(positiveExpo, mod))
        return FieldElement(res.toInt(), prime)
    }
}