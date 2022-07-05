package eater.ai

import com.badlogic.ashley.core.Entity


class GenericAction(
    name: String,
    private val scoreFunction: (entity: Entity) -> Double,
    private val abortFunction: (entity: Entity) -> Unit,
    private val actFunction: (entity: Entity, deltaTime:Float) -> Unit): eater.ai.AiAction(name) {
    override fun abort(entity: Entity) {
        abortFunction(entity)
    }

    override fun act(entity: Entity, deltaTime: Float) {
        actFunction(entity, deltaTime)
    }

    override fun score(entity: Entity): Double {
        return scoreFunction(entity)
    }
}