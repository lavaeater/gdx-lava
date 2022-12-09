package eater.music

import com.badlogic.gdx.math.MathUtils.*
import eater.core.selectedItemListOf
import ktx.math.random

class SoloMusician(name: String, inputSamplers: List<Sampler>, val recordBars: Int = 4, val repeats: Int = 1) :TonalMusician(name, inputSamplers.first()) {

    private var recordedMelody = Array(recordBars * 16) {Note(0, 1f, false)}
    private var repeatBar = -999f
    private val randomRange = 0f..1f

    private val samplers = selectedItemListOf(*inputSamplers.toTypedArray())

    override fun playNote(midiNoteDiff: Int, hitTime: Float) {
        samplers.selectedItem.play(midiNoteDiff, hitTime)
    }

    override fun play(beat: Int, sixteenth: Int, timeBars: Float, hitTime: Float, intensity: Float) {
        if(sixteenth == last16th)
            return

        val wholeBar = floor(timeBars)
        haveOrWillHavePlayed[sixteenth] = false
        val recordingIdx = sixteenth + wholeBar % recordBars * 16

        val repeatEndBars = repeatBar + repeats * recordBars

        if (timeBars < repeatEndBars) {
            val note = recordedMelody[recordingIdx]
            if (note.realNote) {
                haveOrWillHavePlayed[sixteenth] = true
                playNote(note.midiNoteDiff, hitTime)
            }
        } else {
            if (recordingIdx == 0) {
                samplers.nextItem()
                recordedMelody = Array(recordBars * 16) {Note(0, 1f, false)}
            }
            var note: Note? = null

            // always play a strong note on the downbeat
            if (sixteenth == 0) {
                note = getScaleNote(1.0f)
            } else if (sixteenth % 4 == 0) {
                if (randomRange.random() < intensity) {
                    note = getScaleNote(0.5f)
                }
            } else if (sixteenth % 2 === 0) {
                if (randomRange.random() < intensity - 0.25f) {
                    note = getScaleNote(0.25f)
                }
            } else if (randomRange.random() < intensity - 0.5f) {
                note = getScaleNote(0.0f)
            }

            // record and play the note
            recordedMelody[recordingIdx] = note ?: recordedMelody[recordingIdx]

            if (note != null) {
                haveOrWillHavePlayed[sixteenth] = true
                playNote(note.midiNoteDiff, hitTime)
            }

            // if we're done recording, start repeating
            val lastRecordingIdx = recordBars * 16 - 1
            if (recordingIdx >= lastRecordingIdx) {
                repeatBar = kotlin.math.ceil(timeBars)
            }
        }
    }
}