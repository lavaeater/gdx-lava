package eater.ai

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class AiComponent : Component, Pool.Poolable {
    val actions = mutableListOf<AiAction>()
    private var currentAction: AiAction? = null

    fun updateAction(entity: Entity) {
        actions.sortByDescending { it.updateScore(entity) }
    }

    fun topAction(entity: Entity): AiAction? {
        val potentialAction = actions.first()
        if (currentAction != potentialAction) {
            currentAction?.abort(entity)
            currentAction = potentialAction
        }
        return currentAction
    }

    override fun reset() {
        actions.clear()
        currentAction = null
    }

    companion object {
        val mapper = mapperFor<AiComponent>()
        fun get(entity: Entity): AiComponent {
            return mapper.get(entity)
        }
        fun has(entity:Entity): Boolean {
            return mapper.has(entity)
        }
    }
}