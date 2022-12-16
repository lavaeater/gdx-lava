package eater.music

import de.pottgames.tuningfork.SoundSource

class Sampler(private val soundSource: SoundSource) {
    fun play(midiNoteDiff: Int, scheduledTime: Float) {
        ToPlay.soundsToPlay.add(PlayableNote(soundSource, midiNoteDiff.toPitch(), scheduledTime))
    }
}
