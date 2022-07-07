package eater.ai

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.MathUtils

open class Consideration(
    val name: String,
    val scoreFunction: (entity: Entity) -> Float = { 0f },
    val scoreRange: ClosedFloatingPointRange<Float> = 0f..1f
) {
    open fun normalizedScore(entity: Entity): Float {
        return MathUtils.map(0f, 1f, scoreRange.start, scoreRange.endInclusive, scoreFunction(entity))
    }

}