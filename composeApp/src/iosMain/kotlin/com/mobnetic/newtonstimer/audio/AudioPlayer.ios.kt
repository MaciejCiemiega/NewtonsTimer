package com.mobnetic.newtonstimer.audio

import dev.icerock.moko.resources.AssetResource
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.AVFoundation.seekToTime
import platform.AVFoundation.setVolume
import platform.CoreMedia.CMTimeMakeWithSeconds

@OptIn(ExperimentalForeignApi::class)
actual class AudioPlayer {

    private var player: AVPlayer? = null

    actual fun prepare(resource: AssetResource) {
        release()
        player = AVPlayer(uRL = resource.url)
    }

    actual fun setVolume(volume: Float) {
        player?.setVolume(volume)
    }

    actual fun start() {
        player?.apply {
            pause()
            seekToTime(CMTimeMakeWithSeconds(0.0, 1))
            play()
        }
    }

    actual fun release() {
        player?.pause()
    }
}