package eater.music

import com.badlogic.gdx.ai.GdxAI
import de.pottgames.tuningfork.Audio

class MusicPlayer(private val audio: Audio) {
    private val timePiece by lazy { GdxAI.getTimepiece() }
    private val metronome = Metronome(120f)
    private val conductor by lazy {
        Conductor(
            metronome,
            listOf(
                Chord(1f, listOf(
                    Note(-2, .3f),
                    Note(-8, .8f),
                    Note(-5, 1f),
                    Note(0, .5f)
                )),
                Chord(2f, listOf(
                    Note(2, .0f),
                    Note(-1, 1f),
                    Note(0, .7f)
                )),
                Chord(3f, listOf(
                    Note(-2, .3f),
                    Note(0, 1f),
                    Note(4, .5f),
                )),
            ),
            5f,
            listOf(
                Drummer(metronome,1f),
                Bassish(loadSampler("bass-one-shot-808-sicko_C_major", "bass-1.json"),
                metronome,
                1f)
            )) }
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
