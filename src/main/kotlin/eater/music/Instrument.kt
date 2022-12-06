package eater.music

import com.badlogic.gdx.math.MathUtils

abstract class Instrument(override val receiverName: String, protected val sampler: Sampler, val notes: MutableMap<Int, Note>) :
    IMusicSignalReceiver {

    lateinit var chord: Chord
        private set

    override fun setChord(chord: Chord) {
        this.chord = chord
    }

    var last16th = 0
    var lastTimeBars = 0f
    override fun signal(beat: Int, sixteenth: Int, timeBars: Float, hitTime: Float, intensity: Float) {
        last16th = MathUtils.floor(lastTimeBars * 16f) % 16
        lastTimeBars = timeBars
        play(beat, sixteenth, timeBars, hitTime, intensity)
    }

    abstract fun play(beat: Int, sixteenth: Int, timeBars: Float, hitTime: Float, intensity: Float)
}

