package eater.ai

import com.badlogic.ashley.core.Entity
import eater.ai.AiAction

abstract class StaticScoreAction(name: String, private val score: Float) : AiAction(name) {
    override fun score(entity: Entity): Float {
        return score
    }
}