package com.mobnetic.newtonstimer.audio

import korlibs.audio.sound.Sound
import korlibs.audio.sound.readSound
import korlibs.io.file.std.resourcesVfs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

actual class AudioPlayer {

    private var sound: Sound? = null
    private var playScope: CoroutineScope? = null
    private val requestFlow = Channel<Unit>()

    actual fun prepare(audioFile: AudioFile) {
        release()
        playScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        playScope?.launch {
            sound = resourcesVfs[audioFile.path].readSound()

            requestFlow.receiveAsFlow().collect {
                sound?.play()
            }
        }
    }

    actual fun setVolume(volume: Float) {
        sound?.volume = volume.toDouble()
    }

    actual fun start() {
        requestFlow.trySend(Unit)
    }

    actual fun release() {
        playScope?.cancel()
    }
}