package com.mobnetic.newtonstimer.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import platform.UIKit.UIApplication
import platform.UIKit.UIStatusBarStyleDarkContent
import platform.UIKit.UIStatusBarStyleLightContent
import platform.UIKit.setStatusBarStyle

@Composable
actual fun setSystemBarsColor(isLightSystemBarsContent: Boolean) {
    LaunchedEffect(isLightSystemBarsContent) {
        UIApplication.sharedApplication.setStatusBarStyle(
            statusBarStyle = when (isLightSystemBarsContent) {
                true -> UIStatusBarStyleDarkContent
                false -> UIStatusBarStyleLightContent
            }
        )
    }
}

