package eater.music

import de.pottgames.tuningfork.SoundBuffer

class Sampler(private val soundBuffer: SoundBuffer) {
    fun play(midiNoteDiff: Int, scheduledTime: Float) {
        ToPlay.soundsToPlay.add(PlayableNote(soundBuffer, midiNoteDiff.toPitch(), scheduledTime))
    }
}
