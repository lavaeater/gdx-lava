package eater.music

class SignalDrummer(name: String, sampler: Sampler, notes: MutableMap<Int, Note>) : Instrument(name, sampler, notes) {
    override fun play(beat: Int, sixteenth: Int, hitTime: Float, intensity: Float) {
        if (sixteenth == last16th)
            return
        val minIntensity = 1f - intensity
        last16th = sixteenth
        val note = notes[sixteenth]
        if (note != null && note.strength >= minIntensity)
            sampler.play(note.midiNoteDiff, hitTime)
    }
}