package eater.music

data class Chord(val barPos: Float, val chordNotes: Array<Note>, val scaleNotes: Array<Note>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Chord

        if (barPos != other.barPos) return false
        if (!chordNotes.contentEquals(other.chordNotes)) return false
        if (!scaleNotes.contentEquals(other.scaleNotes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = barPos.hashCode()
        result = 31 * result + chordNotes.contentHashCode()
        result = 31 * result + scaleNotes.contentHashCode()
        return result
    }
}