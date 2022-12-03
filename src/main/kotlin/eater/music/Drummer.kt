package eater.music

import com.badlogic.gdx.math.MathUtils.floor


class Drummer(metronome: Metronome, intensity: Float): Musician(metronome,intensity) {
    private val kick by lazy { loadSampler("80PD_KitB-Kick01", "drumkit-1.json") }
    private val snare by lazy { loadSampler("80PD_KitB-Snare02", "drumkit-1.json") }
    private val hat by lazy { loadSampler("80PD_KitB-OpHat02", "drumkit-1.json") }

    private val kickNotes = generateBeat(-12..12, 1,2)
    private val snareNotes = generateBeat(-6..6, 1,4)
    private val hatNotes = generateBeat(-4..8, 1,6)

    private val instrumentsAndNotes = mapOf(snare to snareNotes, kick to kickNotes, hat to hatNotes)
    override fun updateNotes(timeBars: Float, newIntensity: Float) {
        intensity = newIntensity
        val this16th = getThis16th(timeBars)
        val last16th = getLast16th()

        lastTimeBars = timeBars
        if(last16th == this16th)
            return

        val minIntensity = 1f - intensity

        val wholeBar = floor(timeBars)
        val barFraction = this16th / 16f
        val hitTime = metronome.barsToEngineTime(wholeBar + barFraction)
        for((drum, notes) in instrumentsAndNotes) {
            val note = notes[this16th]
            if(note != null && note.strength >= minIntensity)
                drum.play(notes[this16th]!!.midiNoteDiff, hitTime)
        }
    }
}
