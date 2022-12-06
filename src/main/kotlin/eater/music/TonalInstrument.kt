package eater.music

import com.badlogic.gdx.math.MathUtils.floor

sealed class ArpeggioMode(val name: String) {
    object Up:ArpeggioMode("Up")
    object Down:ArpeggioMode("Down")
    object Random:ArpeggioMode("Random")
}

abstract class TonalInstrument(name: String, sampler: Sampler) : Instrument(name, sampler) {

    override fun willPlay(sixteenth: Int, intensity: Float): Boolean {
        val note = getChordNote(1f-intensity)
        return note != null
    }

    open fun getNote(notes: List<Note>, minStrength: Float): Note? {
        val filtered = notes.filter { it.strength >= minStrength }
        return if( filtered.any()) filtered.random() else null
    }

    open fun getChordNote(minStrength: Float): Note? {
        return getNote(chord.chordNotes, minStrength)
    }

    open fun playNote(midiNoteDiff: Int, hitTime: Float) {
        sampler.play(midiNoteDiff, hitTime)
    }
}

class ChimeyChimeChime(name: String, sampler: Sampler, var mode: ArpeggioMode) : TonalInstrument(name, sampler) {
    private var sequenceIndex = 0
    override fun play(beat: Int, sixteenth: Int, timeBars: Float, hitTime: Float, intensity: Float) {
        val stepSize = if(intensity < 0.4f) 4 else if(intensity < 0.7f) 8 else 16
        val lastStep = floor(lastTimeBars * stepSize) % stepSize
        val thisStep = floor(timeBars * stepSize) % stepSize
        if(lastStep == thisStep)
            return
        val wholeBar = floor(timeBars)
        val barFraction = thisStep.toFloat() / stepSize.toFloat()
        val noteTime = wholeBar + barFraction

        val numChordNotes = chord.chordNotes.size
        var chordNoteIndex = 0
        when(mode) {
            ArpeggioMode.Down -> {
                sequenceIndex = (sequenceIndex + 1) % numChordNotes
                chordNoteIndex = numChordNotes - (sequenceIndex + 1)
            }
            ArpeggioMode.Random -> {
                chordNoteIndex = (0..numChordNotes).random()
            }
            ArpeggioMode.Up -> {
                sequenceIndex = (sequenceIndex + 1) % numChordNotes
                chordNoteIndex = sequenceIndex
            }
        }

        sampler.play(chord.chordNotes[chordNoteIndex].midiNoteDiff, noteTime)

    }
}

