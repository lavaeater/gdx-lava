package eater.music

data class Note(val number: Int, var strength: Float) {
    val noteName = MidiNotes.reverseNotes[number]!!
    companion object {
        fun getNote(note: String): Note {
            return Note(MidiNotes.notes[note]!!, 1f)
        }
    }
}

fun String.toNote():Note {
    return Note.getNote(this)
}