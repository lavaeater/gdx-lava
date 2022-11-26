package eater.music

import de.pottgames.tuningfork.SoundBuffer

class Sampler(private val soundBuffer: SoundBuffer) {
    fun play(noteNumber: Int, scheduledTime: Float) {
        ToPlay.soundsToPlay.add(PlayableNote(soundBuffer, noteNumber.toPitch(), scheduledTime))
    }
}
