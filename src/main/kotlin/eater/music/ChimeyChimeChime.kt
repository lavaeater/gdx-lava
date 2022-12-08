package eater.music

import com.badlogic.gdx.math.MathUtils

class ChimeyChimeChime(name: String, sampler: Sampler, var mode: ArpeggioMode) : TonalMusician(name, sampler) {
    private var sequenceIndex = 0
//    var nextPlayTime = 0f
    override fun play(beat: Int, sixteenth: Int, timeBars: Float, hitTime: Float, intensity: Float) {
      //  if(hitTime > nextPlayTime) {
            val stepSize = if (intensity < 0.4f) 4 else if (intensity < 0.7f) 8 else 16
            val lastStep = MathUtils.floor(lastTimeBars * stepSize) % stepSize
            val thisStep = MathUtils.floor(timeBars * stepSize) % stepSize
            if (lastStep == thisStep)
                return
            val wholeBar = MathUtils.floor(timeBars)
            val barFraction = thisStep.toFloat() / stepSize.toFloat()
            val noteTime = wholeBar + barFraction

            val numChordNotes = chord.chordNotes.size
            var chordNoteIndex = 0
            when (mode) {
                ArpeggioMode.Down -> {
                    sequenceIndex = (sequenceIndex + 1) % numChordNotes
                    chordNoteIndex = numChordNotes - (sequenceIndex + 1)
                }

                ArpeggioMode.Random -> {
                    chordNoteIndex = (0..numChordNotes).random()
                }

                ArpeggioMode.Up -> {
                    sequenceIndex = (sequenceIndex + 1) % numChordNotes
                    chordNoteIndex = sequenceIndex
                }
            }

            /**
             * we could simply not allow this sampler to play until a certain time is reached, which would be
             * hitTime + duration
             */

            sampler.play(chord.chordNotes[chordNoteIndex].midiNoteDiff, noteTime)
     //       nextPlayTime = hitTime + sampler.duration
//        }

    }
}