package eater.music

abstract class Musician(protected val metronome: Metronome, var intensity: Float) {
    lateinit var currentChord: Chord
        private set
    var lastTimeBars = 0f
    fun setChord(chord: Chord) {
        currentChord = chord
    }

    abstract fun updateNotes(timeBars: Float)
}