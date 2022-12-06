package eater.music

interface IMusicSignalReceiver {
    val receiverName: String
    fun signal(beat: Int, sixteenth: Int, timeBars: Float, hitTime: Float, intensity: Float)
    fun setChord(chord: Chord)
}