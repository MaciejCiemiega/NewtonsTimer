package com.mobnetic.newtonstimer

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() {
    startDi()

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = MR.strings.app_name.localized()
        ) {
            App()
        }
    }
}