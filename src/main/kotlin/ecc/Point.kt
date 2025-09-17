package org.mist.ecc

import kotlin.math.pow

data class Point(val x:Double,val y:Double,val a:Double,val b:Double) {
    init {
        if(y.pow(2) != x.pow(3) + a*x + b){
           throw IllegalArgumentException(
               "($x,$y) is not on the curve"
           )
        }
    }
}