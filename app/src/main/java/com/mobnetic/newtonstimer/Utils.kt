/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mobnetic.newtonstimer

import java.lang.Math.toDegrees
import java.lang.Math.toRadians
import kotlin.math.atan
import kotlin.math.sin

fun sinDegree(angle: Float): Float = sin(toRadians(angle.toDouble())).toFloat()

fun atanDegree(x: Float): Float = toDegrees(atan(x).toDouble()).toFloat()

fun createAnglesArray(
    ballsCount: Int,
    leftBallAngle: Float = 0f,
    middleBallsAngle: (index: Int) -> Float = { 0f },
    rightBallAngle: Float = 0f
) = FloatArray(ballsCount) { index ->
    when (index) {
        0 -> leftBallAngle
        ballsCount - 1 -> rightBallAngle
        else -> middleBallsAngle(index)
    }
}
