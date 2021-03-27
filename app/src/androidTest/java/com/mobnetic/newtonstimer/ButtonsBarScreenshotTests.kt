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

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.karumi.shot.ScreenshotTest
import com.mobnetic.newtonstimer.timer.ButtonsBar
import com.mobnetic.newtonstimer.timer.TimerState
import com.mobnetic.newtonstimer.timer.TimerViewModel
import com.mobnetic.newtonstimer.ui.theme.MyTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ButtonsBarScreenshotTests : ScreenshotTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var timerViewModel: TimerViewModel

    @Test
    fun rendersTheComponentInNoConfiguredState() {
        renderComponent(TimerState.NotConfigured())

        compareScreenshot(composeTestRule)
    }

    @Test
    fun rendersTheComponentInPausedState() {
        renderComponent(TimerState.Configured.Paused(1000L))

        compareScreenshot(composeTestRule)
    }

    @Test
    fun rendersTheComponentInRunningState() {
        renderComponent(TimerState.Configured.Running(1000L))

        compareScreenshot(composeTestRule)
    }

    private fun renderComponent(state: TimerState) {
        composeTestRule.setContent {
            timerViewModel = viewModel()
            MyTheme(darkMode = timerViewModel.darkMode) {
                ButtonsBar(viewModel = timerViewModel, state = state)
            }
        }
    }
}
