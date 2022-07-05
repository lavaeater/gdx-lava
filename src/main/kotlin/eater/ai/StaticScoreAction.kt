package eater.ai

import com.badlogic.ashley.core.Entity
import eater.ai.AiAction

abstract class StaticScoreAction(name: String, private val score: Float) : eater.ai.AiAction(name) {
    override fun score(entity: Entity): Double {
        return score.toDouble()
    }
}