package eater.music

import com.badlogic.gdx.ai.GdxAI
import de.pottgames.tuningfork.Audio

class MusicPlayer(private val audio: Audio) {
    private val timePiece by lazy { GdxAI.getTimepiece() }
    private val metronome = Metronome(120f)
    private val conductor by lazy { Conductor(metronome, listOf(), 5f, listOf(Drummer(metronome,1f))) }
    private var play = false
    fun toggle() {
        if(play)
            metronome.stop()
        else
            metronome.play()

        play = !play
    }
    fun update(deltaTime: Float) {
        timePiece.update(deltaTime)
        conductor.update()
        val soundsToPlayRightNowIGuess = ToPlay.soundsToPlay.filter { it.targetTime < timePiece.time }
        ToPlay.soundsToPlay.removeAll(soundsToPlayRightNowIGuess)
        for(sound in soundsToPlayRightNowIGuess) {
            audio.play(sound.soundBuffer, 1f, sound.pitch)
        }
    }
}
