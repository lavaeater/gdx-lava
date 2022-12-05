package eater.music

import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.math.MathUtils
import eater.messaging.MessageHandler

interface IMusicSignalReceiver {
    fun signal(sixteenth: Int, timeBars: Float, hitTime: Float)
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
    fun getThis16th(timeBars: Float) = MathUtils.floor(timeBars * 16f) % 16
    fun getLast16th() = MathUtils.floor(lastTimeBars * 16f) % 16
    val timeQuarters: Float get() {
        return if(playing) {
            val timeSeconds = currentTime - startTime
            (tempo / 60) * timeSeconds
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
    fun update() {
        /**
         * we send a signal every sixteenth containing the info needed,
         * I guess
         */
        val this16th = getThis16th(timeBars)
        val last16th = getLast16th()

        lastTimeBars = timeBars
        if(last16th == this16th)
            return

        val wholeBar = MathUtils.floor(timeBars)
        val barFraction = this16th / 16f
        val hitTime = barsToEngineTime(wholeBar + barFraction)
        for(receiver in receivers)
            receiver.signal(this16th, timeBars, hitTime)
    }
}