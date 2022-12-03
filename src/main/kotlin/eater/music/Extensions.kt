package eater.music

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import de.pottgames.tuningfork.WaveLoader
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
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

fun List<String>.generateBeat(top: Int, bottom: Int): Map<Int, Note> {
    val tempo = top.toFloat() / bottom.toFloat() * 16f
    val distance = MathUtils.floor(tempo)
    return (0 until 16 step distance).associateWith { this.random().toNote() }
}

fun Int.toPitch(): Float {
    return ((this - 60) / 12f).pow(2f)
}

fun String.toNote():Note {
    return Note.getNote(this, (5..10).random().toFloat() / 10f)
}

fun String.toNote(strength: Float): Note {
    return Note.getNote(this, strength)
}