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
import com.mobnetic.newtonstimer.createAnglesArray
import com.mobnetic.newtonstimer.timer.TimerState.Configured
import com.mobnetic.newtonstimer.timer.TimerState.Configured.Paused
import com.mobnetic.newtonstimer.timer.TimerState.Configured.Running
import com.mobnetic.newtonstimer.timer.TimerState.NotConfigured
import java.util.concurrent.TimeUnit

class TimerViewModel(application: Application) : AndroidViewModel(application) {

    private val hitSoundPlayer = HitSoundPlayer(application)
    private val ticker = TimerTicker(
        viewModelScope = viewModelScope,
        timerStateProvider = ::state,
        onTick = ::refreshRemainingMillis,
        onBallHit = hitSoundPlayer::playHitSound,
        onTimerFinished = { setNewState(NotConfigured()) }
    )
    private var _state: TimerState by mutableStateOf(NotConfigured())

    var darkMode by mutableStateOf(true)
    var displayedMillis by mutableStateOf(_state.remainingMillis)
    val state get() = _state

    fun configureAngle(angle: Float) {
        if (state is Configured) return
        setNewState(NotConfigured(angle = angle.coerceAtLeast(0f).coerceAtMost(MAX_ANGLE)))
    }

    fun play() {
        val state = state
        when {
            !state.canBeStarted() -> return
            state is NotConfigured -> setNewState(state.started())
            state is Paused -> setNewState(state.resumed())
        }
    }

    fun pause() {
        val runningState = state as? Running ?: return
        setNewState(runningState.paused())
    }

    fun reset() {
        setNewState(NotConfigured())
    }

    fun getAnimationAngles() = when (val state = _state) {
        is Configured -> state.swingAnimation.getAnimationAngles(state, BALLS_COUNT)
        else -> createAnglesArray(BALLS_COUNT, leftBallAngle = state.startAngle)
    }

    override fun onCleared() {
        super.onCleared()
        ticker.cancel()
        hitSoundPlayer.release()
    }

    private fun refreshRemainingMillis() {
        displayedMillis = _state.remainingMillis
    }

    private fun setNewState(newState: TimerState) {
        ticker.onStateChange(newState)
        _state = newState
        refreshRemainingMillis()
    }

    companion object {
        const val MAX_ANGLE = 50f
        val MAX_DURATION_MILLIS = TimeUnit.SECONDS.toMillis(90)
        private const val BALLS_COUNT = 5
    }
}
