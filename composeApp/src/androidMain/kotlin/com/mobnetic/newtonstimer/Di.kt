package com.mobnetic.newtonstimer

import android.app.Application
import com.mobnetic.newtonstimer.audio.AudioPlayer
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun startDi(application: Application) {
    startKoin {
        androidContext(application)
        val androidModule = module {
            singleOf(::AudioPlayer)
        }
        modules(androidModule)
    }
}