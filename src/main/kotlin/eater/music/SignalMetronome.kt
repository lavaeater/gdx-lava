package eater.music

import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.math.MathUtils.floor

class SignalMetronome(
    val tempo: Float,
    val instruments: MutableList<IMusicSignalReceiver> = mutableListOf(),
    val chords: MutableList<Chord> = mutableListOf(),
    val chordLengthBars: Float = 2f) {
    private val timepiece by lazy { GdxAI.getTimepiece() }
    private val currentTime get() = timepiece.time
    private var startTime = 0f
    var playing = false
        private set
    val notPlaying get() = !playing
    fun play() {
        startTime = currentTime
        playing = true
    }

    fun stop() {
        playing = false
    }

    var lastTimeBars = 0f
    val this16th get() = floor(timeBars * 16f) % 16

    val thisBar get() = floor(timeSeconds * (tempo / 60f))

    val last16th get() = floor(lastTimeBars * 16f) % 16
    val timeSeconds get() = if (playing) currentTime - startTime else 0f
    val timeQuarters: Float
        get() {
            return if (playing) {
                (tempo / 60f) * timeSeconds
            } else 0f
        }

    val timeBars: Float
        get() {
            return if (playing) {
                timeQuarters / 4f
            } else 0f
        }

    /** Not sure this is needed now
     *
     */
    fun barsToEngineTime(timeBars: Float): Float {
        val quarters = timeBars * 4
        val seconds = quarters / (tempo / 60)
        return startTime + seconds
    }

    private val minIntensity = 0f
    private val maxIntensity = 1f

    var lastBar = 0
    var intensity = 0.2f
    var change = 0.0005f
    var oddsOfChange = 5

    private fun updateIntensity() {
        if ((0..9).random() > 10 - oddsOfChange) {
            intensity += change
            if (intensity > maxIntensity) {
                intensity = maxIntensity
                change = -change
            }
            if (intensity < minIntensity) {
                intensity = minIntensity
                change = -change
            }
        }
    }

    fun update() {
        /**
         * we send a signal every sixteenth containing the info needed,
         * I guess
         */
        updateIntensity()
        if (last16th != this16th)
            lastTimeBars = timeBars


        val chordTimeBars = timeBars % chordLengthBars
        var currentChord = chords.firstOrNull { it.barPos > chordTimeBars }
        if(currentChord == null)
            currentChord = chords.first()

        val wholeBar = floor(timeBars)
        val barFraction = this16th / 16f
        val hitTime = barsToEngineTime(wholeBar + barFraction)
        for (receiver in instruments) {
            receiver.setChord(currentChord)
            receiver.signal(thisBar, this16th, timeBars, hitTime, intensity)
        }
    }
}