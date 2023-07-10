package com.mobnetic.newtonstimer

import androidx.compose.animation.animateColorAsState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.luminance
import com.mobnetic.newtonstimer.timer.NewtonsTimerScreen
import com.mobnetic.newtonstimer.timer.TimerViewModel
import com.mobnetic.newtonstimer.ui.SystemBarsContentColor
import com.mobnetic.newtonstimer.ui.theme.MyTheme

@Composable
fun App(timerViewModel: TimerViewModel = remember { TimerViewModel() }) {
    MyTheme(darkMode = timerViewModel.darkMode) {
        val backgroundColor by animateColorAsState(MaterialTheme.colors.background)

        val isLightStatusBarContent = backgroundColor.luminance() < 0.5
        SystemBarsContentColor(isLightStatusBarContent)

        Surface(color = backgroundColor) {
            NewtonsTimerScreen(timerViewModel)
        }
    }
}