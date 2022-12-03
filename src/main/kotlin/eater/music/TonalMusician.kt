package eater.music

abstract class TonalMusician(private val sampler: Sampler, metronome: Metronome, intensity: Float):
    Musician(metronome,intensity) {
    open fun getNote(notes: List<Note>, minStrength: Float): Note {
        return notes.filter { it.strength >= minStrength }.random()
    }

    open fun getChordNote(minStrength: Float): Note {
        return getNote(currentChord.chordNotes, minStrength)
    }

    open fun playNote(midiNoteDiff: Int, timeBars: Float) {
        sampler.play(midiNoteDiff, metronome.barsToEngineTime(timeBars))
    }
}