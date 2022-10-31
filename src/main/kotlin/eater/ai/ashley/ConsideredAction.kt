package eater.ai.ashley

import com.badlogic.ashley.core.Entity

class ConsideredAction(
    name: String,
    scoreRange: ClosedFloatingPointRange<Float>,
    private val actFunction: (entity: Entity, deltaTime: Float) -> Unit,
    vararg consideration: Consideration
) : AiAction(name,scoreRange) {
    init {
        considerations.addAll(consideration)
    }

    var abortFunction: (entity: Entity) -> Unit = {}
    override fun abort(entity: Entity) {
        abortFunction(entity)
    }

    override fun act(entity: Entity, deltaTime: Float) {
        actFunction(entity, deltaTime)
    }
}