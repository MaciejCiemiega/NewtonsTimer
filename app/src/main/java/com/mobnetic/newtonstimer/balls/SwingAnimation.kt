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
package com.mobnetic.newtonstimer.balls

import androidx.compose.animation.core.Animation
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.TargetBasedAnimation
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import com.mobnetic.newtonstimer.createAnglesArray
import com.mobnetic.newtonstimer.timer.TimerState
import java.util.concurrent.TimeUnit

class SwingAnimation(maxAngle: Float) {

    private val animation: Animation<Float, AnimationVector1D>

    init {
        animation = TargetBasedAnimation(
            animationSpec = infiniteRepeatable(
                repeatMode = RepeatMode.Reverse,
                animation = tween(
                    durationMillis = CYCLE_DURATION_MILLIS.toInt(),
                    easing = SWING_EASING
                )
            ),
            typeConverter = Float.VectorConverter,
            initialValue = maxAngle,
            targetValue = -maxAngle,
        )
    }

    fun isBallHit(previousElapsedMillis: Long, elapsedMillis: Long): Boolean {
        val previousCycleIndex = (previousElapsedMillis + MILLIS_UNTIL_HIT) / CYCLE_DURATION_MILLIS
        val cycleIndex = (elapsedMillis + MILLIS_UNTIL_HIT) / CYCLE_DURATION_MILLIS
        return previousCycleIndex != cycleIndex
    }

    fun getAnimationAngles(state: TimerState.Configured, ballsCount: Int): FloatArray {
        val angle = state.rawAngle() * state.remainingEnergy
        val angleAfterBeingHit = angle * SWING_ANGLE_AFTER_BEING_HIT_MULTIPLIER
        val reboundAngleAfterHit = angle * SWING_REBOUND_ANGLE_AFTER_HIT_MULTIPLIER
        val isLeftBallSwinging = angle > 0
        val isFirstSwing = state.elapsedMillis <= MILLIS_UNTIL_HIT

        fun getLeftBallAngle() = if (isLeftBallSwinging) angle else reboundAngleAfterHit
        fun getRightBallAngle() = if (!isLeftBallSwinging) angle else reboundAngleAfterHit
        fun getMiddleBallAngle(index: Int): Float {
            val indexDistanceFromTheSwingingBall = (if (isLeftBallSwinging) ballsCount - index else index) - 1
            return angleAfterBeingHit * (1 + indexDistanceFromTheSwingingBall * SWING_ADDITIONAL_BOUNCE_BETWEEN_BALLS_MULTIPLIER)
        }

        return when (isFirstSwing) {
            true -> createAnglesArray(ballsCount, leftBallAngle = getLeftBallAngle())
            else -> createAnglesArray(
                ballsCount,
                leftBallAngle = getLeftBallAngle(),
                middleBallsAngle = ::getMiddleBallAngle,
                rightBallAngle = getRightBallAngle()
            )
        }
    }

    private fun TimerState.Configured.rawAngle(): Float {
        val animationProgressMillis = elapsedMillis
        val elapsedNanos = TimeUnit.MILLISECONDS.toNanos(animationProgressMillis)
        return animation.getValueFromNanos(elapsedNanos)
    }

    private companion object {
        const val CYCLE_DURATION_MILLIS = 500L
        const val SWING_ANGLE_AFTER_BEING_HIT_MULTIPLIER = 0.05f
        const val SWING_REBOUND_ANGLE_AFTER_HIT_MULTIPLIER = -SWING_ANGLE_AFTER_BEING_HIT_MULTIPLIER / 4
        const val SWING_ADDITIONAL_BOUNCE_BETWEEN_BALLS_MULTIPLIER = 0.14f
        const val MILLIS_UNTIL_HIT = CYCLE_DURATION_MILLIS / 2
        val SWING_EASING = CubicBezierEasing(0.7f, 0f, 0.3f, 1f)
    }
}
