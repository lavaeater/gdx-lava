package eater.turbofacts

import factories.factsOfTheWorld

//class FactsOfTheWorldInt<in R>(private val key: String, private val defaultValue: Int = 0) :
//    ReadWriteProperty<R, Int> {
//    private val factsOfTheWorld: TurboFactsOfTheWorld by lazy { factsOfTheWorld() }
//    override fun getValue(thisRef: R, property: KProperty<*>): Int {
//        return factsOfTheWorld.getInt(key)
//    }
//
//    override fun setValue(thisRef: R, property: KProperty<*>, value: Int) {
//        factsOfTheWorld.intOrDefault(defaultValue, key)
//    }
//}
//
//fun intByFacts(defaultValue: Int, vararg key: String) : FactsOfTheWorldInt<R> {
//    return FactsOfTheWorldInt(multiKey(*key), defaultValue)
//}

object FactsLikeThatMan {
    var waveSize : Int
        get() {
            return factsOfTheWorld().intOrDefault(1, Factoids.WaveSize)
        }
        set(value) {
            factsOfTheWorld().setIntFact(value, Factoids.WaveSize)
        }

}

object Factoids {
    const val MaxSpawnedEnemies = "MaxSpawnedEnemies"
    const val MaxEnemies = "MaxEnemies"
    const val NumberOfObjectives = "NumberOfObjectives"
    const val CurrentLevel = "CurrentLevel"
    const val BulletCount = "BulletCount"
    const val EnemyCount = "EnemyCount"
    const val Context = "Context"
    const val MetNumberOfNpcs = "MetNumberOfNpcs"
    const val Score = "Score"
    const val PlayerTileX = "PlayerTileX"
    const val PlayerTileY = "PlayerTileY"
    const val WaveSize = "WaveSize"
    const val StartingEnemyCount = "StartingEnemyCount"


    // New keys for my new, shiny game
    const val BossIsDead = "BossIsDead"
    const val AllObjectivesAreTouched = "AllObjectivesAreTouched"
    const val LevelComplete = "LevelComplete"
    const val GotoNextLevel = "GotoNextLevel"
    const val LevelFailed = "LevelFailed"
    const val EnemyKillCount = "EnemyKillCount"
    const val TargetEnemyKillCount = "TargetEnemyKillCount"
    const val ShowEnemyKillCount = "ShowEnemyKillCount"
    const val AcceleratingSpawns = "AcceleratingSpawns"
    const val AcceleratingSpawnsFactor = "AcceleratingSpawnsFactor"

    //Map Facts
    const val CurrentMapName = "CurrentMapName"
    const val MapStartMessage = "MapStartMessage"
    const val MapSuccessMessage = "MapSuccessMessage"
    const val MapFailMessage = "MapFailMessage"
    const val LevelStarted = "LevelStarted"

    //PlayerFacts
    const val LivingPlayerCount = "LivingPlayerCount"
}