package eater.ai

import com.badlogic.ashley.core.Entity
import eater.ai.AiAction

abstract class StaticScoreAction(name: String, score: Float) : AiAction(name) {
    init {
        this.score = score
    }
    override fun updateScore(entity: Entity): Float {
        return score
    }
}