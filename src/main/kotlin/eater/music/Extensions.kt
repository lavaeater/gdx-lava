package eater.music

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.MathUtils.norm
import de.pottgames.tuningfork.WaveLoader
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.math.absoluteValue

fun <K, V> Map<K, V>.reversed() = HashMap<V, K>().also { newMap ->
    entries.forEach { newMap[it.value] = it.key }
}

fun loadSampler(name: String, instrument: String, baseDir:String): Sampler {
    if(!InstrumentsCache.instruments.containsKey(instrument)) {
        val json = Gdx.files.local("instruments/$instrument").readString()
        val instruments = Json.decodeFromString<List<ListItem.SoundFile>>(json)
        InstrumentsCache.instruments[instrument] = instruments
    }
    val instruments = InstrumentsCache.instruments[instrument]!!
    val soundFile = instruments.first { it.name == name }
    return Sampler(WaveLoader.load(Gdx.files.external("$baseDir/${soundFile.path}")))
}

fun generateBeat(midiNoteSpan: IntRange, top: Int, bottom: Int, shift: Int = 0, strengthRange: IntRange = (3..8)): MutableMap<Int, Note> {
    val tempo = top.toFloat() / bottom.toFloat() * 16f
    val distance = MathUtils.floor(tempo)
    return (0 until 16 step distance).map { if(it + shift > 15) 0 + shift else if(it + shift < 0) 15 + shift else it + shift }
        .associateWith { Note(midiNoteSpan.random(), strengthRange.random().toFloat() / 10f) }
        .toMutableMap()
}

/**
 * Takes an integer between -12 and 12 and converts
 * it to a pitch value corresponding to 0.5 and 2.0.
 */
fun Int.toPitch(): Float {
    val minPitch = -12
    val maxPitch = 12
    /**
     * Hmm. So, -12 is 0.5f in pitch,
     * + 12 is 2.0f
     *
     * 0 is 1f
     *
     *
     */
    return if (this < 0) {
        if(this < minPitch)
            0.5f
        else
            1f - (1f / (maxPitch * 2 / this.absoluteValue.toFloat()))
    } else if(this > 0) {
        if(this > maxPitch)
            2f
        else {
            1f + norm(0f, 12f, this.toFloat())
        }
    } else {
        1f
    }
}