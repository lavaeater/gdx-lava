package eater.music

class SignalDrummer(name: String, sampler: Sampler, val notes: MutableMap<Int, Note>) : Instrument(name, sampler) {
    override fun play(beat: Int, sixteenth: Int, timeBars: Float, hitTime: Float, intensity: Float) {
        if (sixteenth == last16th)
            return
        val minIntensity = 1f - intensity
        last16th = sixteenth
        val note = notes[sixteenth]
        if (note != null && note.strength >= minIntensity)
            sampler.play(note.midiNoteDiff, hitTime)
    }

    override fun willPlay(sixteenth: Int, intensity: Float): Boolean {
        val note = notes[sixteenth]
        return note != null && note.strength >= 1f-intensity
    }
}