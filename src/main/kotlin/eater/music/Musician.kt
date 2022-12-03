package eater.music

import com.badlogic.gdx.math.MathUtils.floor
import ktx.math.random

abstract class Musician(protected val metronome: Metronome, var intensity: Float) {
    lateinit var currentChord: Chord
        private set
    var lastTimeBars = 0f
    fun setChord(chord: Chord) {
        currentChord = chord
    }

    abstract fun updateNotes(timeBars: Float, newIntensity: Float)
    protected val last16th get() = floor(lastTimeBars * 16f) % 16
    protected fun getThis16th(timeBars: Float) = floor(timeBars * 16f) % 16
}

open class Bassish(sampler: Sampler, metronome: Metronome, intensity: Float) : TonalMusician(sampler, metronome, intensity) {
    override fun updateNotes(timeBars: Float, newIntensity: Float) {
        val this16th = getThis16th(timeBars)
        lastTimeBars = timeBars
        if (last16th == this16th)
            return

        val wholeBar = floor(timeBars)
        val barFraction = this16th / 16f
        val noteTime = wholeBar + barFraction
        if (this16th == 0) {
            playNote(getChordNote(1f).number, noteTime)
            return
        }

        if (this16th % 4 == 0) {
            if ((0f..1f).random() < intensity)
                playNote(getChordNote(0.5f).number, noteTime)
            return
        }

        if (this16th % 2 == 0) {
            if ((0f..1f).random() < intensity - 0.25f)
                playNote(getChordNote(0.25f).number, noteTime)
            return
        }
        if ((0f..1f).random() < intensity - 0.5f)
            playNote(getChordNote(0f).number, noteTime)
    }
}

class Arpeggiator(sampler: Sampler, metronome: Metronome, intensity: Float) :
    TonalMusician(sampler, metronome, intensity) {
    override fun updateNotes(timeBars: Float, newIntensity: Float) {
        TODO("Not yet implemented")
    }

}