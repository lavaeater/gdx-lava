package eater.music

import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.math.MathUtils.floor

class SignalConductor(
    val tempo: Float,
    val instruments: MutableList<IMusicSignalReceiver> = mutableListOf(),
    val chords: MutableList<Chord>) {
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
    var baseIntensity = 0.2f
    val baseChange = 0.005f
    var change = 0.005f
    var chanceOfChange = 45

    private fun updateIntensity() {
        val randomValue = (0..99).random()
        if(randomValue < 3) {
            change *= 2f
        } else if((4..6).contains(randomValue)) {
            change = -change
        } else if((11..15).contains(randomValue)) {
            change = if(change < 0f) -baseChange else baseChange
        }

        if(randomValue < chanceOfChange) {
            baseIntensity += change
            if (baseIntensity > maxIntensity) {
                change = -change
                baseIntensity = maxIntensity
            }
            if (baseIntensity < minIntensity) {
                change = -change
                baseIntensity = minIntensity
            }
        }
    }
    var currentChord = chords.first()

    fun update() {
        /**
         * we send a signal every sixteenth containing the info needed,
         * I guess
         */
        if(playing) {
            //updateIntensity()
            if (last16th != this16th)
                lastTimeBars = timeBars


            val chordTimeBars = timeBars % chords.size
            var nextChord = chords.firstOrNull { it.barPos > chordTimeBars }
            if (nextChord == null)
                nextChord = chords.first()

            currentChord = nextChord

            val wholeBar = floor(timeBars)
            val barFraction = this16th / 16f
            val hitTime = barsToEngineTime(wholeBar + barFraction)
            for (receiver in instruments) {
                receiver.setChord(currentChord)
                receiver.signal(thisBar, this16th, timeBars, hitTime, baseIntensity)
            }
        }
    }
}