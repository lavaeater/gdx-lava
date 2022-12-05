package eater.music

import com.badlogic.gdx.ai.GdxAI

/**
 * Perhaps change to something that sends out pulses instead? To the instruments?
 *
 * Then the instruments would just have to play their sounds, if it is the correct signal.
 */

class Metronome(val tempo: Float) {
    private val timepiece by lazy { GdxAI.getTimepiece() }
    private val currentTime get() = timepiece.time
    private var startTime = 0f
    var playing = false
        private set
    val notPlaying get() = !playing

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

