package eater.music

class Conductor(private val metronome: Metronome, private val chords: Array<Chord>, private val chordLengthBars: Float, private val musicians: Array<Musician>) {

    val minIntensity = 0.2f
    val maxIntensity = 1f
    var intensity = 0.75f
    var change = 0.05f

    fun updateIntensity() {
        intensity += change
        if(intensity > maxIntensity) {
            intensity = maxIntensity
            change = -change
        }
        if(intensity < minIntensity) {
            intensity = minIntensity
            change = -change
        }
    }
    fun update() {
        if(metronome.notPlaying)
            return

        updateIntensity()
        val timeBars = metronome.timeBars
        val chordTimeBars = timeBars % chordLengthBars
        //val currentChord = chords.first { it.barPos > chordTimeBars }
        for (musician in musicians) {
          //  musician.setChord(currentChord)
            musician.updateNotes(timeBars, intensity)
        }
    }
}