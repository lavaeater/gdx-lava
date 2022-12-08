package eater.music

interface IMusicSignalReceiver {
    val receiverName: String
    fun signal(beat: Int, sixteenth: Int, timeBars: Float, hitTime: Float, baseIntensity: Float)
    fun setChord(chord: Chord)
}