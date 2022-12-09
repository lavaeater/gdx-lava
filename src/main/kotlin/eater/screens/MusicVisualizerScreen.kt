package eater.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.MathUtils.clamp
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import eater.core.MainGame
import eater.extensions.boundLabel
import eater.extensions.boundProgressBar
import eater.injection.InjectionContext
import eater.input.CommandMap
import eater.music.*
import ktx.actors.stage
import ktx.scene2d.*
import space.earlygrey.shapedrawer.ShapeDrawer

class MusicVisualizerScreen(game: MainGame) : BasicScreen(game, CommandMap("MyCommands")) {

    private val sampleBaseDir = "projects/games/music-samples-explorer"

    override val viewport: Viewport = ExtendViewport(400f, 600f)

    private val kickSampler by lazy { loadSampler("Kick", "drums-1.json", sampleBaseDir) }
    private val snareSampler by lazy { loadSampler("Snare", "drums-1.json", sampleBaseDir) }
    private val hatSampler by lazy { loadSampler("ClHat", "drums-1.json", sampleBaseDir) }
    private val bassSampler by lazy { loadSampler("lofi-bass", "lo-fi-1.json", sampleBaseDir) }
    private val rythmGuitarSampler by lazy { loadSampler("rythm-guitar-c", "guitar-1.json", sampleBaseDir) }
    private val soloSampler by lazy { loadSampler("lead-c", "lo-fi-1.json", sampleBaseDir) }
    private val leadSampler by lazy { loadSampler("fxpad", "lo-fi-1.json", sampleBaseDir) }
    private val kickBeat = floatArrayOf(
        1f, -1f, -1f, 0.1f,
        0.4f, -1f, 0.4f, -1f,
        0.9f, -1f, -1f, 0.2f,
        0.5f, -1f, 0.3f, -1f
    ).mapIndexed { i, s -> i to Note(0, s) }.toMap().toMutableMap()

//    private val superBassBeat = floatArrayOf(
//        1f, 0f, 0f, 0.1f,
//        0.4f, 0f, 0.4f, 0f,
//        0.9f, 0f, 0f, 0.2f,
//        0.5f, 0f, 0.3f, 0f
//    ).mapIndexed { i, s -> i to Note(0, s) }.toMap().toMutableMap()

    private val snareBeat = floatArrayOf(
        -1f, 0.1f, -1f, 0.2f,
        1f, -1f, 0.5f, -1f,
        -1f, 0.2f, -1f, 0.2f,
        1f, -1f, 0.25f, 0.1f
    ).mapIndexed { i, s -> i to Note(0, s) }.toMap().toMutableMap()

    private val hatBeat = floatArrayOf(
        1f, 0.1f, 0.9f, 0.3f,
        1f, 0.5f, 0.7f, 0.4f,
        1f, 0.2f, 0.8f, 0.3f,
        1f, 0.4f, 0.7f, 0.1f
    ).mapIndexed { i, s -> i to Note(0, s) }.toMap().toMutableMap()

    private val scaleNotes = listOf(
        Note(-1, 1f),
        Note(1, 0.2f),
        Note(3, 0.6f),
        Note(4, 0.5f),
        Note(6, 0.7f),
        Note(8, 0.4f),
        Note(9, 0f),
    )

    private val signalConductor =
        SignalConductor(
            80f,
            4f,
            4f,
            mutableListOf(
                SignalDrummer("kick", kickSampler, kickBeat),
                SignalDrummer("snare", snareSampler, snareBeat),
                SignalDrummer("hat", hatSampler, hatBeat),
                SignalBass("bass", bassSampler),
                SoloMusician("soolooo", listOf(leadSampler)),
                SoloMusician("soolooo", listOf(soloSampler, rythmGuitarSampler)),
            ),
            generateChords()
        )

    private fun generateChords(): MutableList<Chord> {
        return mutableListOf(
            Chord(
                0f,
                listOf(
                    Note(-1, 1f),
                    Note(3, 0.25f),
                    Note(7, 0.5f),
                    Note(11, 0f),
                ), scaleNotes

            ),
            Chord(
                1f,
                listOf(
                    Note(-3, 1f),
                    Note(1, 0.25f),
                    Note(4, 0.5f),
                    Note(7, 0f),
                ), scaleNotes
            ),
            Chord(
                2f,
                listOf(
                    Note(-5, 1f),
                    Note(-1, 0.25f),
                    Note(2, 0.5f),
                    Note(5, 0f),
                ), scaleNotes
            ),
            Chord(
                3f,
                listOf(
                    Note(0, 1f),
                    Note(2, 0.25f),
                    Note(4, 0.5f),
                    Note(6, 0f),
                ), scaleNotes
            ),
        )
    }

    private val timePiece by lazy { GdxAI.getTimepiece() }

    private fun setUpCommands() {
        commandMap.setUp(Input.Keys.SPACE, "Toggle signalplayer") {
            if (signalConductor.notPlaying)
                signalConductor.play()
            else
                signalConductor.stop()
        }

        commandMap.setUp(Input.Keys.RIGHT, "Intensity UP") {
            signalConductor.baseIntensity = clamp(signalConductor.baseIntensity + 0.1f, 0f, 1f)
        }
        commandMap.setUp(Input.Keys.LEFT, "Intensity DOWN") {
            signalConductor.baseIntensity = clamp(signalConductor.baseIntensity - 0.1f, 0f, 1f)
        }
    }

    override fun show() {


        Scene2DSkin.defaultSkin = Skin(Gdx.files.internal("ui/uiskin.json"))
        super.show()
        if (::stage.isInitialized) {
            stage.clear()
            stage.dispose()
        }
        stage = getStage()
        setUpCommands()
    }

    override fun render(delta: Float) {
        super.render(delta)
        timePiece.update(delta)
        stage.act(delta)
        stage.draw()
        signalConductor.update()
        playSounds()
    }

    private var volume = 0.5f

    private fun playSounds() {
        val soundsToPlayRightNowIGuess = ToPlay.soundsToPlay.filter { it.targetTime < timePiece.time }
        ToPlay.soundsToPlay.removeAll(soundsToPlayRightNowIGuess)
        for (sound in soundsToPlayRightNowIGuess) {
            if (sound.soundSource.isPlaying)
                sound.soundSource.stop()
            sound.soundSource.setPitch(sound.pitch)
            sound.soundSource.setVolume(volume)
            sound.soundSource.play()
        }
    }


    private lateinit var stage: Stage
    private val shapeDrawer by lazy { InjectionContext.inject<ShapeDrawer>() }

    private fun getStage(): Stage {
        return stage(batch, viewport).apply {
            actors {
                table {
                    table {
                        boundProgressBar({ signalConductor.baseIntensity }).cell(pad = 5f)
                        row()
                        boundLabel({ "Bar: ${signalConductor.thisBar}: ${signalConductor.lastBar}" })
                        row()
                        boundLabel({ "Chord: ${signalConductor.currentChord.barPos}: ${signalConductor.chords.size}" })
                        row()
                        table {
                            for (r in 0..signalConductor.instruments.size) {
                                if (r == 0) {
                                    (0..signalConductor.notesPerMeasure).forEach { c ->
                                        if (c == 0)
                                            label("Instrument")
                                        else
                                            container { label(" $c ") }
                                                .apply {
                                                    background = object : BaseDrawable(), IMusicSignalReceiver {
                                                        val index = c - 1
                                                        var on = false
                                                        val color = Color(0f, 0f, 0f, 0f)
                                                        override val receiverName: String
                                                            get() = "Color changing background"

                                                        override fun signal(
                                                            beat: Int,
                                                            thisNoteIndex: Int,
                                                            timeBars: Float,
                                                            hitTime: Float,
                                                            baseIntensity: Float
                                                        ) {
                                                            if (thisNoteIndex == index) {
                                                                on = true
                                                            }
                                                        }

                                                        override fun setChord(chord: Chord) {

                                                        }

                                                        override fun updateSignature(
                                                            beatsPerMeasure: Float,
                                                            beatDuration: Float
                                                        ) {

                                                        }

                                                        override fun draw(
                                                            batch: Batch,
                                                            x: Float,
                                                            y: Float,
                                                            width: Float,
                                                            height: Float
                                                        ) {
                                                            if (on) {
                                                                color.set(Color.GREEN)
                                                                on = false
                                                            } else {
                                                                color.set(
                                                                    clamp(color.r + 0.1f, 0f, 1f),
                                                                    clamp(color.g - 0.1f, 0f, 1f),
                                                                    0f,
                                                                    clamp(color.a - 0.1f, 0f, 1f)
                                                                )
                                                            }
                                                            shapeDrawer.filledRectangle(x, y, width, height, color)
                                                        }
                                                    }.apply { signalConductor.instruments.add(this) }
                                                }
                                    }
                                } else {
                                    val instrument = signalConductor.instruments[r - 1]
                                    (0..signalConductor.notesPerMeasure).forEach { c ->
                                        if (c == 0)
                                            label(instrument.receiverName)
                                        else
                                            container { label("  ") }
                                                .apply {
                                                    background = object : BaseDrawable(), IMusicSignalReceiver {
                                                        val index = c - 1
                                                        var on = false
                                                        val color = Color(0f, 0f, 0f, 0f)
                                                        override val receiverName: String
                                                            get() = "Color changing background"

                                                        override fun signal(
                                                            beat: Int,
                                                            thisNoteIndex: Int,
                                                            timeBars: Float,
                                                            hitTime: Float,
                                                            baseIntensity: Float
                                                        ) {
                                                            if (index == thisNoteIndex && instrument is Musician) {
                                                                on = instrument.willPlay(thisNoteIndex, baseIntensity)
                                                            }
                                                        }

                                                        override fun setChord(chord: Chord) {

                                                        }

                                                        override fun updateSignature(
                                                            beatsPerMeasure: Float,
                                                            beatDuration: Float
                                                        ) {

                                                        }

                                                        override fun draw(
                                                            batch: Batch,
                                                            x: Float,
                                                            y: Float,
                                                            width: Float,
                                                            height: Float
                                                        ) {
                                                            if (on) {
                                                                color.set(Color.GREEN)
                                                                on = false
                                                            } else {
                                                                color.set(
                                                                    clamp(color.r + 0.01f, 0f, 1f),
                                                                    clamp(color.g - 0.01f, 0f, 1f),
                                                                    0f,
                                                                    clamp(color.a - 0.01f, 0f, 1f)
                                                                )
                                                            }
                                                            shapeDrawer.filledRectangle(
                                                                x,
                                                                y,
                                                                width,
                                                                height,
                                                                color
                                                            )
                                                        }
                                                    }.apply { signalConductor.instruments.add(this) }
                                                }
                                    }
                                }
                                row()
                            }
                        }
                        row()
                    }.align(Align.center)
                    row()
                    setFillParent(true)
                }
            }
        }
    }

}
