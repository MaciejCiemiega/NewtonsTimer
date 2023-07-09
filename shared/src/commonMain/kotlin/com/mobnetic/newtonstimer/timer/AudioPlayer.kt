package com.mobnetic.newtonstimer.timer

import dev.icerock.moko.resources.AssetResource

expect class AudioPlayer {
    fun prepare(resource: AssetResource)
    fun setVolume(volume: Float)
    fun start()
    fun release()
}