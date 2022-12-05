package eater.music

open class Instrument(private val sampler: Sampler, private val notes: MutableMap<Int, Note>) :
    IMusicSignalReceiver {

    var last16th = 0
    override fun signal(beat: Int, sixteenth: Int, timeBars: Float, hitTime: Float, intensity: Float) {
        play(beat, sixteenth, hitTime, intensity)
    }

    open fun play(beat: Int, sixteenth: Int, hitTime: Float, intensity: Float) {
        if(sixteenth == last16th)
            return
        val minIntensity = 1f - intensity
        last16th = sixteenth
            val note = notes[sixteenth]
            if(note != null && note.strength >= minIntensity)
                sampler.play(note.midiNoteDiff, hitTime)
    }
}