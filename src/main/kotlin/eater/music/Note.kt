package eater.music

data class Note(val number: Int, var strength: Float) {
    val noteName = MidiNotes.reverseNotes[number]!!
    companion object {
        fun getNote(note: String, strength: Float): Note {
            return Note(MidiNotes.notes[note]!!, strength)
        }
    }
}

fun String.toNote():Note {
    return Note.getNote(this, (5..10).random().toFloat() / 10f)
}

fun String.toNote(strength: Float): Note {
    return Note.getNote(this, strength)
}