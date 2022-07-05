package eater.ai

import com.badlogic.ashley.core.Entity

class ConsideredAction(
    name: String,
    private val abortFunction: (entity: Entity) -> Unit,
                       private val actFunction: (entity: Entity, deltaTime:Float) -> Unit,
                       vararg consideration: Consideration
): eater.ai.AiAction(name) {
    init {
        considerations.addAll(consideration)
    }
    override fun abort(entity: Entity) {
        abortFunction(entity)
    }

    override fun act(entity: Entity, deltaTime: Float) {
        actFunction(entity, deltaTime)
    }
}