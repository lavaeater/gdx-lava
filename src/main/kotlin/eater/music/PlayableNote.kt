package eater.music

import de.pottgames.tuningfork.SoundBuffer

data class PlayableNote(val soundBuffer: SoundBuffer, val pitch: Float, val targetTime: Float)