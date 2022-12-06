package eater.music

import com.badlogic.gdx.math.MathUtils
import ktx.math.random

class SignalBass(name: String, sampler: Sampler, notes: MutableMap<Int, Note>) : TonalInstrument(name, sampler, notes) {
    override fun play(beat: Int, sixteenth: Int, timeBars: Float, hitTime: Float, intensity: Float) {
        if (last16th == sixteenth)
            return

        val wholeBar = MathUtils.floor(timeBars)
        val barFraction = sixteenth / 16f
        val noteTime = wholeBar + barFraction
        if (sixteenth == 0) {
            val n = getChordNote(1f)
            if (n != null) {
                playNote(n.midiNoteDiff, noteTime)
                return
            }
        }

        if (sixteenth % 4 == 0) {
            if ((0f..1f).random() < intensity) {
                val n = getChordNote(0.5f)
                if (n != null) {
                    playNote(n.midiNoteDiff, noteTime)
                    return
                }
            }
        }

        if (sixteenth % 2 == 0) {
            if ((0f..1f).random() < intensity - 0.25f) {
                val n = getChordNote(0.25f)
                if (n != null) {
                    playNote(n.midiNoteDiff, noteTime)
                    return
                }
            }
        }
        if ((0f..1f).random() < intensity - 0.5f) {
            val n = getChordNote(0f)
            if (n != null) {
                playNote(n.midiNoteDiff, noteTime)
            }
        }
    }
}