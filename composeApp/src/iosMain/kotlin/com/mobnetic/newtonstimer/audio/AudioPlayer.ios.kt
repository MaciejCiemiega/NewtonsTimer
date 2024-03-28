package com.mobnetic.newtonstimer.audio

import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.AVFoundation.seekToTime
import platform.AVFoundation.setVolume
import platform.CoreMedia.CMTimeMakeWithSeconds
import platform.Foundation.NSBundle

@OptIn(ExperimentalForeignApi::class)
actual class AudioPlayer {

    private var player: AVPlayer? = null

    actual fun prepare(audioFile: AudioFile) {
        release()

        audioFile.url?.let {
            player = AVPlayer(uRL = it)
        }
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

private val AudioFile.url
    get() = NSBundle.mainBundle.URLForResource(
        name = fileName,
        withExtension = extension,
        subdirectory = "$composeResourcedDirName/$filesDirName"
    )