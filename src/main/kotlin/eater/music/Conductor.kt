package eater.music

class Conductor(private val metronome: Metronome,
                private val chords: List<Chord>,
                private val chordLengthBars: Float, private val musicians: List<Musician>) {

    private val minIntensity = 0f
    private val maxIntensity = 1f
    var intensity = 0.5f
    var change = 0.0005f

    private fun updateIntensity() {
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
        var currentChord = chords.firstOrNull { it.barPos > chordTimeBars }
        if(currentChord == null)
            currentChord = chords.first()
        for (musician in musicians) {
            musician.setChord(currentChord)
            musician.updateNotes(timeBars, intensity)
        }
    }
}