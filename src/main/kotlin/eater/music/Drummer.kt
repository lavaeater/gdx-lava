package eater.music

import com.badlogic.gdx.math.MathUtils.floor


class Drummer(metronome: Metronome, intensity: Float): Musician(metronome,intensity) {
    private val kick by lazy { loadSampler("80PD_KitB-Kick01", "drumkit-1.json") }
    private val snare by lazy { loadSampler("80PD_KitB-Snare02", "drumkit-1.json") }
    private val hat by lazy { loadSampler("80PD_KitB-OpHat02", "drumkit-1.json") }

    private val kickNotes = listOf("c4", "a4").generateBeat(1,4)
    private val snareNotes = listOf("c4", "d4", "e4", "f4", "g4", "a4").generateBeat(1,8)
    private val hatNotes = listOf("c4", "d4", "e4", "f4", "g4", "a4").generateBeat(1,12)

    private val instrumentsAndNotes = mapOf(kick to kickNotes, snare to snareNotes, hat to hatNotes)
    override fun updateNotes(timeBars: Float, newIntensity: Float) {
        intensity = newIntensity
        val this16th = getThis16th(timeBars)

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
                drum.play(notes[this16th]!!.number, hitTime)
        }
    }

}
