package eater.music

import com.badlogic.gdx.math.MathUtils
import ktx.math.random

open class Bassish(sampler: Sampler, metronome: Metronome, intensity: Float) : TonalMusician(sampler, metronome, intensity) {
    override fun updateNotes(timeBars: Float, newIntensity: Float) {
        val this16th = getThis16th(timeBars)
        lastTimeBars = timeBars
        if (last16th == this16th)
            return

        val wholeBar = MathUtils.floor(timeBars)
        val barFraction = this16th / 16f
        val noteTime = wholeBar + barFraction
        if (this16th == 0) {
            playNote(getChordNote(1f).midiNoteDiff, noteTime)
            return
        }

        if (this16th % 4 == 0) {
            if ((0f..1f).random() < intensity)
                playNote(getChordNote(0.5f).midiNoteDiff, noteTime)
            return
        }

        if (this16th % 2 == 0) {
            if ((0f..1f).random() < intensity - 0.25f)
                playNote(getChordNote(0.25f).midiNoteDiff, noteTime)
            return
        }
        if ((0f..1f).random() < intensity - 0.5f)
            playNote(getChordNote(0f).midiNoteDiff, noteTime)
    }
}