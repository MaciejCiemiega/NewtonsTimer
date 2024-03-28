package com.mobnetic.newtonstimer.audio

import dev.icerock.moko.resources.AssetResource
import org.w3c.dom.Audio

actual class AudioPlayer {

    private var audio: Audio? = null

    actual fun prepare(resource: AssetResource) {
        release()

        audio = Audio(resource.originalPath)
    }

    actual fun setVolume(volume: Float) {
        audio?.volume = volume.toDouble()
    }

    actual fun start() {
        audio?.play()
    }

    actual fun release() {
        audio?.pause()
        audio = null
    }
}