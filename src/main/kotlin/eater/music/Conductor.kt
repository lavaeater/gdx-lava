package eater.music

class Conductor(private val metronome: Metronome, private val chords: Array<Chord>, private val chordLengthBars: Float, private val musicians: Array<Musician>) {
    fun update() {
        if(metronome.notPlaying)
            return

        val timeBars = metronome.timeBars
        val chordTimeBars = timeBars % chordLengthBars
        val currentChord = chords.first { it.barPos > chordTimeBars }
        for (musician in musicians) {
            musician.setChord(currentChord)
            musician.updateNotes(timeBars)
        }
    }
}