package eater.music

data class Note(val number: Int, var strength: Float) {
    val noteName = MidiNotes.reverseNotes[number]!!
    companion object {
        fun getNote(note: String, strength: Float): Note {
            return Note(MidiNotes.notes[note]!!, strength)
        }
    }
}

