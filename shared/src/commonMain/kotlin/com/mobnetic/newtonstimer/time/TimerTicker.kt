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
package com.mobnetic.newtonstimer.time

import com.mobnetic.newtonstimer.tickerFlow
import com.mobnetic.newtonstimer.timer.TimerState
import com.mobnetic.newtonstimer.timer.TimerState.Configured.Running
import com.mobnetic.newtonstimer.timer.absoluteRemainingEnergy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.time.Duration.Companion.milliseconds

class TimerTicker(
    private val viewModelScope: CoroutineScope,
    private val timerStateProvider: () -> TimerState,
    private val onTick: () -> Unit,
    private val onBallHit: (volume: Float) -> Unit,
    private val onTimerFinished: () -> Unit
) {
    private val ticker = tickerFlow(period = TICK_DURATION_MILLIS)
    private var timerJob: Job? = null

    fun onStateChange(newState: TimerState) {
        val oldState = timerStateProvider()
        if (oldState is Running && newState !is Running) {
            timerJob?.cancel()
        } else if (oldState !is Running && newState is Running) {
            timerJob = newTimerJob(newState)
        }
    }

    private fun onTick(previousElapsedMillis: Long): Long {
        val state = timerStateProvider()
        if (state !is Running) return 0
        if (state.isFinished) {
            onTimerFinished()
            return 0
        }

        val elapsedMillis = state.elapsedMillis
        onTick()
        if (state.swingAnimation.isBallHit(elapsedMillis, previousElapsedMillis)) {
            onBallHit(state.absoluteRemainingEnergy)
        }
        return elapsedMillis
    }

    private fun newTimerJob(state: Running): Job {
        var previousElapsedMillis = state.elapsedMillis
        return ticker.onEach {
            previousElapsedMillis = onTick(previousElapsedMillis)
        }.launchIn(viewModelScope)
    }

    fun cancel() {
        timerJob?.cancel()
        timerJob = null
    }

    private companion object {
        val TICK_DURATION_MILLIS = 100.milliseconds
    }
}
