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
package com.mobnetic.newtonstimer.timer

import android.os.SystemClock
import com.mobnetic.newtonstimer.balls.SwingAnimation
import com.mobnetic.newtonstimer.timer.TimerViewModel.Companion.MAX_ANGLE
import com.mobnetic.newtonstimer.timer.TimerViewModel.Companion.MAX_DURATION_MILLIS

sealed class TimerState(val durationMillis: Long) {
    val startAngle: Float = durationToAngle(durationMillis)
    abstract val elapsedMillis: Long

    val remainingMillis get() = (durationMillis - elapsedMillis).coerceAtLeast(0)

    abstract fun withNewDuration(newDurationMillis: Long): TimerState

    class NotConfigured(durationMillis: Long = 0) : TimerState(durationMillis) {
        override val elapsedMillis = 0L

        constructor(angle: Float) : this(durationMillis = angleToDuration(angle))

        override fun withNewDuration(newDurationMillis: Long) = NotConfigured(
            durationMillis = newDurationMillis
        )

        fun started() = Configured.Running(durationMillis = durationMillis)
    }

    sealed class Configured(durationMillis: Long) : TimerState(durationMillis) {

        val swingAnimation = SwingAnimation(startAngle)
        val isFinished get() = remainingMillis <= 0

        val remainingEnergy get() = remainingMillis / durationMillis.toFloat()

        class Paused(
            durationMillis: Long,
            override val elapsedMillis: Long = 0
        ) : Configured(durationMillis) {
            override fun withNewDuration(newDurationMillis: Long) = Paused(
                durationMillis = newDurationMillis,
                elapsedMillis = elapsedMillis
            )

            fun resumed() = Running(
                durationMillis = durationMillis,
                alreadyElapsedMillis = elapsedMillis
            )
        }

        class Running(
            durationMillis: Long,
            private val alreadyElapsedMillis: Long = 0
        ) : Configured(durationMillis) {
            private val resumedAtMillis = SystemClock.uptimeMillis()
            override val elapsedMillis get() = alreadyElapsedMillis + SystemClock.uptimeMillis() - resumedAtMillis

            override fun withNewDuration(newDurationMillis: Long) = Running(
                durationMillis = newDurationMillis,
                alreadyElapsedMillis = elapsedMillis
            )

            fun paused() = Paused(
                durationMillis = durationMillis,
                elapsedMillis = elapsedMillis
            )
        }
    }

    protected companion object {
        fun durationToAngle(durationMillis: Long) = durationMillis / MAX_DURATION_MILLIS.toFloat() * MAX_ANGLE
        fun angleToDuration(angle: Float): Long {
            val duration = (angle / MAX_ANGLE * MAX_DURATION_MILLIS).toLong()
            return duration - duration % 1000
        }
    }
}
