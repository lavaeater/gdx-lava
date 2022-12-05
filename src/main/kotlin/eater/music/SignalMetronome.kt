package eater.music

import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.math.MathUtils.floor

interface IMusicSignalReceiver {
    fun signal(beat: Int, sixteenth: Int, timeBars: Float, hitTime: Float, intensity: Float)
}

class SignalMetronome(val tempo: Float, val receivers: MutableList<IMusicSignalReceiver> = mutableListOf()) {
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
    val timeSeconds get() = if(playing) currentTime - startTime else 0f
    val timeQuarters: Float get() {
        return if(playing) {
            (tempo / 60f) * timeSeconds
        } else 0f
    }

    val timeBars: Float get() {
        return if(playing) {
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

    var lastBar = 0
    var intensity = 0.5f

    fun update() {
        /**
         * we send a signal every sixteenth containing the info needed,
         * I guess
         */


        if(last16th == this16th)
            return

        lastTimeBars = timeBars

        val wholeBar = floor(timeBars)
        val barFraction = this16th / 16f
        val hitTime = barsToEngineTime(wholeBar + barFraction)
        for(receiver in receivers) {
            receiver.signal(thisBar, this16th, timeBars, hitTime, intensity)
        }
    }
}