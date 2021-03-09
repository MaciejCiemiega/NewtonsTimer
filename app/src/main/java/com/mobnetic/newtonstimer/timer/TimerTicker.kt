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

import com.mobnetic.newtonstimer.timer.TimerState.Configured.Running
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.launch

class TimerTicker(
    private val viewModelScope: CoroutineScope,
    private val timerStateProvider: () -> TimerState,
    private val onTick: () -> Unit,
    private val onBallHit: (volume: Float) -> Unit,
    private val onTimerFinished: () -> Unit
) {
    @OptIn(ObsoleteCoroutinesApi::class)
    private val tickerChannel = ticker(delayMillis = TICK_DURATION_MILLIS, initialDelayMillis = 0)
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
            onBallHit(state.remainingEnergy)
        }
        return elapsedMillis
    }

    private fun newTimerJob(state: Running): Job {
        var previousElapsedMillis = state.elapsedMillis
        return viewModelScope.launch(Dispatchers.Default) {
            for (tick in tickerChannel) {
                previousElapsedMillis = onTick(previousElapsedMillis)
            }
        }
    }

    fun cancel() {
        timerJob?.cancel()
        timerJob = null
        tickerChannel.cancel()
    }

    private companion object {
        private const val TICK_DURATION_MILLIS = 100L
    }
}
