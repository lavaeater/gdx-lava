package eater.music

import com.badlogic.gdx.ai.GdxAI

data class Note(val number: Int, val strength: Float)

data class Chord(val barPos: Float, val chordNotes: Array<Note>, val scaleNotes: Array<Note>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Chord

        if (barPos != other.barPos) return false
        if (!chordNotes.contentEquals(other.chordNotes)) return false
        if (!scaleNotes.contentEquals(other.scaleNotes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = barPos.hashCode()
        result = 31 * result + chordNotes.contentHashCode()
        result = 31 * result + scaleNotes.contentHashCode()
        return result
    }
}

class Metronome(private val tempo: Float) {
    private val timepiece by lazy { GdxAI.getTimepiece() }
    private val currentTime get() = timepiece.time
    private var startTime = 0f
    var playing = false
        private set
    val notPlaying get() = !playing

    val timeBars: Float get() {
        return if(playing) {
            val timeSeconds = currentTime - startTime
            val timeQuarters = (tempo / 60) * timeSeconds
            return timeQuarters / 4f
        } else 0f
    }

    fun barsToEngineTime(timeBars: Float): Float {
        val quarters = timeBars * 4
        val seconds = quarters / (tempo / 60)
        return startTime + seconds
    }

    fun play() {
        startTime = currentTime
        playing = true
    }

    fun stop() {
        playing = false
    }
}

abstract class Musician(protected val metronome: Metronome, var intensity: Float) {
    lateinit var currentChord: Chord
        private set
    var lastTimeBars = 0f
    fun setChord(chord: Chord) {
        currentChord = chord
    }

    abstract fun updateNotes(timeBars: Float)
}

class Drummer(metronome: Metronome, intensity: Float):Musician(metronome,intensity) {
    override fun updateNotes(timeBars: Float) {
        TODO("Not yet implemented")
    }
}

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