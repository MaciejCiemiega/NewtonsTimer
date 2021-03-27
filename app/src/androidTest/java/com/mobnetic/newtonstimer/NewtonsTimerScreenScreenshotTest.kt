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

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.down
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.moveTo
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performGesture
import androidx.compose.ui.test.up
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.karumi.shot.ScreenshotTest
import com.mobnetic.newtonstimer.timer.Clock
import com.mobnetic.newtonstimer.timer.TimerTicker
import com.mobnetic.newtonstimer.timer.TimerViewModel
import com.mobnetic.newtonstimer.ui.theme.MyTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.math.roundToInt

@RunWith(AndroidJUnit4::class)
class NewtonsTimerScreenScreenshotTest : ScreenshotTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private var refStartTime: Long = 0

    @Before
    fun setUp() {
        configureClock()
    }

    @Test
    fun showsTheUIReadyToStartTheTimer() {
        renderScreen()

        compareScreenshot(composeTestRule)
    }

    @Test
    fun togglesTheUIToDarkModeWhenTappingOnDarkModeButton() {
        renderScreen()

        toggleDarkMode()

        compareScreenshot(composeTestRule)
    }

    @Test
    fun pullsTheFirstBallUpToTheLimitInOrderToConfigureTheCountDown() {
        renderScreen()

        pullTimerTrigger()

        compareScreenshot(composeTestRule)
    }

    @Test
    fun pullsTheFirstBallHalfTheWayInOrderToConfigureADifferentCountDown() {
        renderScreen()

        pullTimerTrigger(0.5f)

        compareScreenshot(composeTestRule)
    }

    @Test
    fun startsTheCountDownWithTheMaxValueAvailable() {
        renderScreen()

        pullTimerTriggerAndRelease()

        compareScreenshot(composeTestRule)
    }

    @Test
    fun startsTheCountDownWithLessThanTheMaxValueAvailable() {
        renderScreen()

        pullTimerTriggerAndRelease(0.75f)

        compareScreenshot(composeTestRule)
    }

    @Test
    fun startsTheCountDownAndLetsTheTimerRun() {
        renderScreen()

        pullTimerTriggerAndRelease()
        advanceSystemClock(1500)

        compareScreenshot(composeTestRule)
    }

    @Test
    fun startsTheCountDownAndLetsTheTimerRunForAlmostSixtySeconds() {
        renderScreen()

        pullTimerTriggerAndRelease()
        advanceSystemClock(57500)

        compareScreenshot(composeTestRule)
    }

    @Test
    fun startsTheCountDownAndLetsTheTimerRunForSixtySeconds() {
        renderScreen()

        pullTimerTriggerAndRelease()
        advanceSystemClock(60000)

        compareScreenshot(composeTestRule)
    }

    @Test
    fun triggersTheDarkModeToggleEvenWhenTheTimerIsRunning() {
        renderScreen()

        pullTimerTriggerAndRelease()
        advanceSystemClock(30000)
        toggleDarkMode()

        compareScreenshot(composeTestRule)
    }

    @Test
    fun pausesTheTimerWhenTappingOnThePauseButton() {
        renderScreen()

        pullTimerTriggerAndRelease()
        advanceSystemClock(30000)
        tapPauseButton()

        compareScreenshot(composeTestRule)
    }

    @Test
    fun pausesTheTimerWhenTappingOnThePauseButtonAndContinuesTheCountLater() {
        renderScreen()

        pullTimerTriggerAndRelease()
        advanceSystemClock(30000)
        tapPauseButton()
        tapPlayButton()
        advanceSystemClock(15000)

        compareScreenshot(composeTestRule)
    }

    @Test
    fun restartsTheTimerAfterTappingOnThePauseAndTheCloseButtons() {
        renderScreen()

        pullTimerTriggerAndRelease()
        advanceSystemClock(30000)
        tapPauseButton()
        tapResetButton()

        compareScreenshot(composeTestRule)
    }

    private fun tapPauseButton() {
        composeTestRule.onNode(hasTestTag(TestTags.pauseButton)).performClick()
        advanceTime(200)
    }

    private fun tapPlayButton() {
        tapPauseButton()
    }

    private fun tapResetButton() {
        composeTestRule.onNode(hasTestTag(TestTags.resetButton)).performClick()
        advanceTime()
    }

    private fun pullTimerTriggerAndRelease(percentage: Float = 1.0f) {
        pullTimerTrigger(percentage, true)
    }

    private fun pullTimerTrigger(percentage: Float = 1.0f, releaseAfterPull: Boolean = false) {
        composeTestRule.onNode(hasTestTag(TestTags.draggableBall)).performGesture {
            val x0 = (visibleSize.width * (1 - 0.083f)).roundToInt().toFloat()
            val x1 = 0.0f + (x0 * (1f - percentage))
            val y0 = (visibleSize.height * (1 - 0.083f)).roundToInt().toFloat()
            val y1 = 0.0f + (x0 * (1f - percentage))
            val start = Offset(x0, y0)
            val end = Offset(x1, y1)
            if (releaseAfterPull) {
                down(start)
                moveTo(end)
                up()
            } else {
                down(start)
                moveTo(end)
            }
        }
        advanceTime(200L)
    }

    private fun toggleDarkMode() {
        composeTestRule.onNode(hasTestTag(TestTags.darkModeToggleButton)).performClick()
        advanceTime()
    }

    private fun advanceSystemClock(time: Long) {
        val currentTime = Clock.fakeNow
        if (currentTime == null) {
            Clock.fakeNow = time
        } else {
            Clock.fakeNow = currentTime + time
        }
        advanceTime(time)
    }

    private fun configureClock() {
        composeTestRule.mainClock.autoAdvance = false
        TimerTicker.TICK_DURATION_MILLIS = 1
        advanceSystemClock(refStartTime)
    }

    private fun advanceTime(timeInMs: Long = 1000) {
        composeTestRule.mainClock.advanceTimeBy(timeInMs)
    }

    private fun renderScreen() {
        composeTestRule.setContent {
            val timerViewModel: TimerViewModel = viewModel()
            MyTheme(darkMode = timerViewModel.darkMode) {
                MyApp()
            }
        }
    }
}
