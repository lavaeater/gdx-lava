package eater.music

import com.badlogic.gdx.math.MathUtils.floor


class Drummer(metronome: Metronome, intensity: Float): Musician(metronome,intensity) {
    private val kick by lazy { loadSampler("80PD_KitB-Kick01", "drumkit-1.json") }
    private val snare by lazy { loadSampler("80PD_KitB-Snare02", "drumkit-1.json") }
    private val hat by lazy { loadSampler("80PD_KitB-OpHat02", "drumkit-1.json") }

    private val kickNotes = listOf("c4", "a4").generateBeat(1,4)
    private val snareNotes = listOf("c4", "d4", "e4", "f4", "g4", "a4").generateBeat(4,8)
    private val hatNotes = listOf("c4", "d4", "e4", "f4", "g4", "a4").generateBeat(3,8)

    private val instrumentsAndNotes = mapOf(kick to kickNotes, snare to snareNotes, hat to hatNotes)
    override fun updateNotes(timeBars: Float) {
        val last16th = floor(lastTimeBars * 16f) % 16
        val this16th = floor(timeBars * 16f) % 16

        lastTimeBars = timeBars
        if(last16th == this16th)
            return

        val wholeBar = floor(timeBars)
        val barFraction = this16th / 16f
        val hitTime = metronome.barsToEngineTime(wholeBar + barFraction)
        for((drum, notes) in instrumentsAndNotes) {
            if(notes.containsKey(this16th))
                drum.play(notes[this16th]!!.number, hitTime)
        }
    }
}
