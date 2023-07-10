package com.mobnetic.newtonstimer.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.Window
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import com.mobnetic.newtonstimer.ui.theme.Colors

@Composable
actual fun SystemBarsContentColor(isLightSystemBarsContent: Boolean) {
    val window = LocalContext.current.getActivity()?.window

    LaunchedEffect(window, isLightSystemBarsContent) {
        window?.setSystemBarsLightContent(isLightSystemBarsContent)
    }
}

private fun Window.setSystemBarsLightContent(isLight: Boolean) {
    val insetsController = WindowCompat.getInsetsController(this, decorView)
    insetsController.isAppearanceLightStatusBars = !isLight
    insetsController.isAppearanceLightNavigationBars = !isLight

    val systemBarsColor = when (isLight) {
        true -> Colors.systemBarsScrimDark
        false -> Colors.systemBarsScrimLight
    }
    statusBarColor = systemBarsColor.toArgb()
    navigationBarColor = systemBarsColor.toArgb()
}

private fun Context.getActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}