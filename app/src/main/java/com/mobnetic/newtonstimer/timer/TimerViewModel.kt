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

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mobnetic.newtonstimer.timer.TimerState.Configured
import com.mobnetic.newtonstimer.timer.TimerState.Configured.Paused
import com.mobnetic.newtonstimer.timer.TimerState.Configured.Running
import com.mobnetic.newtonstimer.timer.TimerState.NotConfigured
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class TimerViewModel(application: Application) : AndroidViewModel(application) {

    private val hitSoundPlayer = HitSoundPlayer(application)
    @OptIn(ObsoleteCoroutinesApi::class)
    private val tickerChannel = ticker(delayMillis = TICK_DURATION_MILLIS, initialDelayMillis = 0)
    private var timerJob: Job? = null

    private var _state: TimerState by mutableStateOf(NotConfigured())

    var darkMode by mutableStateOf(true)
    var displayedMillis by mutableStateOf(_state.remainingMillis)
    val state get() = _state

    fun configureAngle(angle: Float) {
        if (getAndEnsureState() is Configured) return
        setNewState(NotConfigured(angle = angle.coerceAtLeast(0f).coerceAtMost(MAX_ANGLE)))
    }

    fun play() {
        if (state.durationMillis <= 0) return
        when (val state = getAndEnsureState()) {
            is NotConfigured -> setNewState(state.started())
            is Paused -> setNewState(state.resumed())
        }
    }

    fun pause() {
        val runningState = getAndEnsureState() as? Running ?: return
        setNewState(runningState.paused())
    }

    fun reset() {
        setNewState(NotConfigured())
    }

    fun getAnimationAngles() = when (val state = _state) {
        is Configured -> state.swingAnimation.getAnimationAngles(state)
        else -> FloatArray(BALLS_COUNT)
    }

    override fun onCleared() {
        super.onCleared()
        tickerChannel.cancel()
        hitSoundPlayer.safeRelease()
    }

    private suspend fun onTick() {
        val state = getAndEnsureState() as? Running ?: return
        refreshRemainingMillis()

        if (state.swingAnimation.isBallHit(state, tickDuration = TICK_DURATION_MILLIS)) {
            hitSoundPlayer.playHitSound(volume = state.remainingEnergy)
        }
    }

    private fun newTimerJob() = viewModelScope.launch(Dispatchers.Default) {
        for (tick in tickerChannel) {
            onTick()
        }
    }

    private fun refreshRemainingMillis() {
        displayedMillis = _state.remainingMillis
    }

    private fun setNewState(newState: TimerState) {
        if (_state is Running && newState !is Running) {
            timerJob?.cancel()
        } else if (_state !is Running && newState is Running) {
            timerJob = newTimerJob()
        }
        _state = newState
        refreshRemainingMillis()
    }

    private fun getAndEnsureState(): TimerState {
        val state = _state
        if (state is Configured && state.isFinished) {
            onTimerFinished()
        }
        return _state
    }

    private fun onTimerFinished() {
        setNewState(NotConfigured())
    }

    companion object {
        const val MAX_ANGLE = 50f
        val MAX_DURATION_MILLIS = TimeUnit.SECONDS.toMillis(90)
        const val BALLS_COUNT = 5
        private const val TICK_DURATION_MILLIS = 100L
    }
}
