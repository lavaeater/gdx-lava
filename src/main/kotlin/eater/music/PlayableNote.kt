package eater.music

import de.pottgames.tuningfork.SoundSource

data class PlayableNote(val soundSource: SoundSource, val pitch: Float, val targetTime: Float)