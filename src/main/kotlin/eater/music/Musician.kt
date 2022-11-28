package eater.music

abstract class Musician(protected val metronome: Metronome, var intensity: Float) {
    lateinit var currentChord: Chord
        private set
    var lastTimeBars = 0f
    fun setChord(chord: Chord) {
        currentChord = chord
    }

    abstract fun updateNotes(timeBars: Float, newIntensity: Float)
}

abstract class TonalMusician(protected val sampler: Sampler, metronome: Metronome, intensity: Float):Musician(metronome,intensity) {
    open fun getNote(notes: List<Note>, minStrength: Float): Note {
        return notes.filter { it.strength > minStrength }.random()
    }

    open fun getChordNote(minStrength: Float):Note {
        return getNote(currentChord.chordNotes, minStrength)
    }

    open fun getScaleNote(minStrength: Float):Note {
        return getNote(currentChord.scaleNotes, minStrength)
    }

    open fun playNote(noteNumber: Int, timeBars: Float) {
        sampler.play(noteNumber, metronome.barsToEngineTime(timeBars))
    }
}

class Bass(sampler: Sampler, metronome: Metronome, intensity: Float):TonalMusician(sampler, metronome, intensity) {
    override fun updateNotes(timeBars: Float, newIntensity: Float) {
        TODO("Not yet implemented")
    }

}