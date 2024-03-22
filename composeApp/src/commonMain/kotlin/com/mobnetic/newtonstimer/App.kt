package com.mobnetic.newtonstimer

import androidx.compose.animation.animateColorAsState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.luminance
import com.mobnetic.newtonstimer.timer.NewtonsTimerScreen
import com.mobnetic.newtonstimer.timer.NewtonsTimerViewModel
import com.mobnetic.newtonstimer.ui.SystemBarsContentColor
import com.mobnetic.newtonstimer.ui.theme.MyTheme
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

@Composable
fun App() {
    val timerViewModel = getViewModel(key = Unit, viewModelFactory {
        NewtonsTimerViewModel()
    })

    MyTheme(darkMode = timerViewModel.darkMode) {
        val backgroundColor by animateColorAsState(MaterialTheme.colors.background)

        val isLightStatusBarContent = backgroundColor.luminance() < 0.5
        SystemBarsContentColor(isLightStatusBarContent)

        Surface(color = backgroundColor) {
            NewtonsTimerScreen(timerViewModel)
        }
    }
}