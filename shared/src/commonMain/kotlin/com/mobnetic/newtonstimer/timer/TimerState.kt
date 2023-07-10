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

import com.mobnetic.newtonstimer.balls.SwingAnimation
import com.mobnetic.newtonstimer.time.Clock
import com.mobnetic.newtonstimer.timer.TimerState.Configured.Paused
import com.mobnetic.newtonstimer.timer.TimerState.Configured.Running
import com.mobnetic.newtonstimer.timer.TimerState.NotConfigured
import com.mobnetic.newtonstimer.timer.TimerViewModel.Companion.MAX_ANGLE
import com.mobnetic.newtonstimer.timer.TimerViewModel.Companion.MAX_DURATION_MILLIS

sealed class TimerState(val durationMillis: Long) {
    val startAngle: Float = durationToAngle(durationMillis)
    val remainingMillis get() = (durationMillis - elapsedMillis).coerceAtLeast(0)

    abstract val elapsedMillis: Long

    class NotConfigured(durationMillis: Long = 0) : TimerState(durationMillis) {
        override val elapsedMillis = 0L

        constructor(startAngle: Float) : this(durationMillis = angleToDuration(startAngle))
    }

    sealed class Configured(durationMillis: Long) : TimerState(durationMillis) {
        val swingAnimation = SwingAnimation(startAngle)
        val isFinished get() = remainingMillis <= 0

        class Paused(
            durationMillis: Long,
            override val elapsedMillis: Long = 0
        ) : Configured(durationMillis)

        class Running(
            durationMillis: Long,
            private val alreadyElapsedMillis: Long = 0
        ) : Configured(durationMillis) {
            private val resumedAtMillis = Clock.now()
            override val elapsedMillis get() = alreadyElapsedMillis + Clock.now() - resumedAtMillis
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

val TimerState.Configured.absoluteRemainingEnergy get() = remainingMillis / MAX_DURATION_MILLIS.toFloat()
val TimerState.Configured.relativeRemainingEnergy get() = remainingMillis / durationMillis.toFloat()
fun TimerState.canBeStarted() = durationMillis > 0
fun NotConfigured.started() = Running(durationMillis = durationMillis)
fun Paused.resumed() = Running(durationMillis = durationMillis, alreadyElapsedMillis = elapsedMillis)
fun Running.paused() = Paused(durationMillis = durationMillis, elapsedMillis = elapsedMillis)
