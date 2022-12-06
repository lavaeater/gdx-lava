package eater.music

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import de.pottgames.tuningfork.WaveLoader
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.math.absoluteValue
import kotlin.math.pow

fun <K, V> Map<K, V>.reversed() = HashMap<V, K>().also { newMap ->
    entries.forEach { newMap[it.value] = it.key }
}

fun loadSampler(name: String, instrument: String): Sampler {
    if(!InstrumentsCache.instruments.containsKey(instrument)) {
        val json = Gdx.files.local(instrument).readString()
        val instruments = Json.decodeFromString<List<ListItem.SoundFile>>(json)
        InstrumentsCache.instruments[instrument] = instruments
    }
    val instruments = InstrumentsCache.instruments[instrument]!!
    val soundFile = instruments.first { it.name == name }
    return Sampler(WaveLoader.load(Gdx.files.external(soundFile.path)))
}

fun generateBeat(midiNoteSpan: IntRange, top: Int, bottom: Int, strengthRange: IntRange = (0..10)): MutableMap<Int, Note> {
    val tempo = top.toFloat() / bottom.toFloat() * 16f
    val distance = MathUtils.floor(tempo)
    return (0 until 16 step distance).associateWith { Note(midiNoteSpan.random(), strengthRange.random().toFloat() / 10f) }.toMutableMap()
}

fun Int.toPitch(): Float {
    /**
     * Hmm. So, -12 is 0.5f in pitch,
     * + 12 is 2.0f
     *
     * 0 is 1f
     *
     *
     */
    return if (this < 0) {
        if(this < -12)
            0.5f
        else
            1f - (1f / (24f / this.absoluteValue.toFloat()))
    } else if(this > 0) {
        if(this > 12)
            2f
        else {
            24f / this.toFloat()
        }
    } else {
        1f
    }
}