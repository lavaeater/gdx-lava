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
    protected fun getThis16th(timeBars: Float) = floor(timeBars * 16f) % 16
    protected fun getLast16th() = floor(lastTimeBars * 16f) % 16
}

class Arpeggiator(sampler: Sampler, metronome: Metronome, intensity: Float) :
    TonalMusician(sampler, metronome, intensity) {
    override fun updateNotes(timeBars: Float, newIntensity: Float) {
        TODO("Not yet implemented")
    }

}