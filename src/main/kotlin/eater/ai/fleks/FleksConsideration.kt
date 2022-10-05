package eater.ai.fleks

import com.github.quillraven.fleks.Entity

open class FleksConsideration(
    val name: String,
    val scoreFunction: (entity: Entity) -> Float = { 0f }
) {
    open fun normalizedScore(entity: Entity): Float {
        return scoreFunction(entity)
    }

}