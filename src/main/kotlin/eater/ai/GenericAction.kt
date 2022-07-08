package eater.ai

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.MathUtils


class GenericAction(
    name: String,
    private val scoreFunction: (entity: Entity) -> Float,
    private val abortFunction: (entity: Entity) -> Unit,
    private val actFunction: (entity: Entity, deltaTime:Float) -> Unit,
    scoreRange: ClosedFloatingPointRange<Float> = 0f..1f): AiAction(name, scoreRange) {
    override fun abort(entity: Entity) {
        abortFunction(entity)
    }

    override fun act(entity: Entity, deltaTime: Float) {
        actFunction(entity, deltaTime)
    }

    override fun score(entity: Entity): Float {
        return MathUtils.map(0f,1f, scoreRange.start, scoreRange.endInclusive, scoreFunction(entity))
    }
}