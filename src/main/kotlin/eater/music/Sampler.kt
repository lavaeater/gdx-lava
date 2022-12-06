package eater.music

import de.pottgames.tuningfork.SoundBuffer

class Sampler(private val soundBuffer: SoundBuffer) {
    val duration get() = soundBuffer.duration
    fun play(midiNoteDiff: Int, scheduledTime: Float) {
        ToPlay.soundsToPlay.add(PlayableNote(soundBuffer, midiNoteDiff.toPitch(), scheduledTime))
    }
}
