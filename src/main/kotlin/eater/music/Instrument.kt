package eater.music

import eater.messaging.IMessage
import eater.messaging.IMessageReceiver

open class Instrument(private val sampler: Sampler) : IMusicSignalReceiver {
    val beats = mutableMapOf(0 to 0, 4 to -2, 8 to 2, 12 to 0)
    override fun signal(sixteenth: Int, timeBars: Float, hitTime: Float) {
        play(sixteenth, hitTime)
    }

    open fun play(sixteenth: Int, hitTime: Float) {
        if (beats.contains(sixteenth)) {
            sampler.play(beats[sixteenth]!!, hitTime)
        }
    }
}