package com.mobnetic.newtonstimer.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import com.mobnetic.newtonstimer.ui.theme.Colors

@Composable
actual fun setSystemBarsColor(isLightSystemBarsContent: Boolean) {
    val window = LocalContext.current.getActivity()?.window
    LaunchedEffect(window, isLightSystemBarsContent) {
        if (window != null) {
            val insetsController = WindowCompat.getInsetsController(window, window.decorView)
            insetsController.isAppearanceLightStatusBars = !isLightSystemBarsContent
            insetsController.isAppearanceLightNavigationBars = !isLightSystemBarsContent

            val systemBarsColor = when (isLightSystemBarsContent) {
                true -> Colors.systemBarsScrimDark
                false -> Colors.systemBarsScrimLight
            }
            window.statusBarColor = systemBarsColor.toArgb()
            window.navigationBarColor = systemBarsColor.toArgb()
        }
    }
}

private fun Context.getActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}