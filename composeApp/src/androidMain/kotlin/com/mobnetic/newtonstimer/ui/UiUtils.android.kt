package com.mobnetic.newtonstimer.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat

@Composable
actual fun SystemBarsContentColor(isLightSystemBarsContent: Boolean) {
    val window = LocalContext.current.getActivity()?.window

    LaunchedEffect(window, isLightSystemBarsContent) {
        if (window != null) {
            WindowCompat.getInsetsController(window, window.decorView).run {
                isAppearanceLightStatusBars = !isLightSystemBarsContent
                isAppearanceLightNavigationBars = !isLightSystemBarsContent
            }
        }
    }
}

private fun Context.getActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}