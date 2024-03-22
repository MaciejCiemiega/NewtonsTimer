package com.mobnetic.newtonstimer.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import platform.UIKit.UIApplication
import platform.UIKit.UIStatusBarStyleDarkContent
import platform.UIKit.UIStatusBarStyleLightContent
import platform.UIKit.setStatusBarStyle

@Composable
actual fun SystemBarsContentColor(isLightSystemBarsContent: Boolean) {
    LaunchedEffect(isLightSystemBarsContent) {
        val systemBarStyle = when (isLightSystemBarsContent) {
            true -> UIStatusBarStyleLightContent
            false -> UIStatusBarStyleDarkContent
        }
        UIApplication.sharedApplication.setStatusBarStyle(systemBarStyle)
    }
}