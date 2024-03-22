package com.mobnetic.newtonstimer

import com.mobnetic.newtonstimer.audio.AudioPlayer
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun startDi() {
    startKoin {
        val desktopModule = module {
            singleOf(::AudioPlayer)
        }
        modules(desktopModule)
    }
}