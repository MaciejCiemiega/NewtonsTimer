package com.mobnetic.newtonstimer

import java.lang.Math.toDegrees
import java.lang.Math.toRadians
import kotlin.math.atan
import kotlin.math.sin

fun sinDegree(angle: Float): Float = sin(toRadians(angle.toDouble())).toFloat()
fun atanDegree(x: Float): Float = toDegrees(atan(x).toDouble()).toFloat()
