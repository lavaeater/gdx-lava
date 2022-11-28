package eater.music

abstract class TonalMusician(protected val sampler: Sampler, metronome: Metronome, intensity: Float):
    Musician(metronome,intensity) {
    open fun getNote(notes: List<Note>, minStrength: Float): Note {
        return notes.filter { it.strength > minStrength }.random()
    }

    open fun getChordNote(minStrength: Float): Note {
        return getNote(currentChord.chordNotes, minStrength)
    }

    open fun getScaleNote(minStrength: Float): Note {
        return getNote(currentChord.scaleNotes, minStrength)
    }

    open fun playNote(noteNumber: Int, timeBars: Float) {
        sampler.play(noteNumber, metronome.barsToEngineTime(timeBars))
    }
}