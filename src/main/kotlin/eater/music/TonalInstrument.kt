package eater.music

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

