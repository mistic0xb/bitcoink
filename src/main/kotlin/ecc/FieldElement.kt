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

    fun add(other: FieldElement): FieldElement {
        if (prime != other.prime) {
            throw IllegalArgumentException(
                "Cannot add two numbers in different Fields"
            )
        }
        val newNum = (num + other.num) % prime
        return FieldElement(newNum, prime)
    }

    fun sub(other: FieldElement): FieldElement {
        if (prime != other.prime) {
            throw IllegalArgumentException(
                "Cannot subtract two numbers in different Fields"
            )
        }
        val res = (num - other.num) % prime
        return FieldElement(res, prime)
    }

    fun mul(other: FieldElement): FieldElement {
        if (prime != other.prime) {
            throw IllegalArgumentException(
                "Cannot multiply two numbers in different Fields"
            )
        }
        val res = (num * other.num) % prime
        return FieldElement(res, prime)
    }

    fun div(other: FieldElement): FieldElement {
        if (prime != other.prime) {
            throw IllegalArgumentException(
                "Cannot divide two numbers in different Fields"
            )
        }
        val res = (num * (other.power(prime - 2).num)) % prime
        return FieldElement(res, prime)
    }

    fun power(exp: Int): FieldElement {
        val n = exp % (prime - 1)
        val base = BigInteger.valueOf(num.toLong())
        val mod = BigInteger.valueOf(prime.toLong())
        val positiveExpo = BigInteger.valueOf(n.toLong())
        val res = (base.modPow(positiveExpo, mod))
        return FieldElement(res.toInt(), prime)
    }
}