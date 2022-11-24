package eater.music

import com.badlogic.gdx.ai.GdxAI

class Metronome(val tempo: Float) {
    private val timepiece by lazy { GdxAI.getTimepiece() }
    private val currentTime get() = timepiece.time
    private var startTime = 0f
    private var playing = false

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