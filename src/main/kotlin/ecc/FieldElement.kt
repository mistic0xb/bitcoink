package org.mist.ecc

import java.math.BigInteger

data class FieldElement(val num: BigInteger, val prime: BigInteger) {
    constructor(num: Int, prime: Int) : this(
        num = BigInteger.valueOf(num.toLong()),
        prime = BigInteger.valueOf(prime.toLong())
    )

    init {
        if (num >= prime || num < BigInteger.ZERO) {
            throw IllegalArgumentException(
                "Num $num not in field range 0 to ${prime - BigInteger.ONE}"
            )
        }
    }

    override fun toString(): String {
        return "FieldElement_$prime($num)"
    }

    private fun mod(value: BigInteger, modulus: BigInteger): BigInteger {
        val result = value % modulus
        if (result < BigInteger.ZERO) {
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
        val res = mod((num * (other.pow(prime - 2.toBigInteger()).num)), prime)
        return FieldElement(res, prime)
    }

    fun pow(exp: BigInteger): FieldElement {
        val n = mod(exp, (prime - BigInteger.ONE))
        val base = BigInteger.valueOf(num.toLong())
        val mod = BigInteger.valueOf(prime.toLong())
        val positiveExpo = BigInteger.valueOf(n.toLong())
        val res = (base.modPow(positiveExpo, mod))
        return FieldElement(res, prime)
    }
}