package com.mobnetic.newtonstimer

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import newtonstimer.composeapp.generated.resources.Res
import newtonstimer.composeapp.generated.resources.app_name
import org.jetbrains.compose.resources.stringResource

fun main() {
    startDi()

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = stringResource(Res.string.app_name)
        ) {
            App()
        }
    }
}