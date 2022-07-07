package eater.ai

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import eater.core.engine
import eater.ecs.components.Memory
import eater.ecs.components.TransformComponent
import ktx.ashley.allOf
import kotlin.reflect.KClass
import kotlin.reflect.full.starProjectedType

class AmICloseToThisConsideration<ToLookFor : Component, ToStoreIn : Memory>(
    private val lookFor: KClass<ToLookFor>,
    private val toStoreIn: KClass<ToStoreIn>,
    private val distance: Float
) : Consideration("Am I Close to This?") {
    private val entitiesToLookForFamily = allOf(lookFor, TransformComponent::class).get()
    private val engine by lazy { engine() }
    override fun normalizedScore(entity: Entity): Float {
        val position = TransformComponent.get(entity).position
        val closeBums = engine.getEntitiesFor(entitiesToLookForFamily)
            .filter { TransformComponent.get(it).position.dst(position) < distance }
        if(closeBums.any()) {
            val memory = ensureMemory(entity, lookFor, toStoreIn)
            if (!memory.closeEntities.containsKey(lookFor.starProjectedType)) {
                memory.closeEntities[lookFor.starProjectedType] = mutableListOf()
            } else {
                memory.closeEntities[lookFor.starProjectedType]!!.clear()
            }
            memory.closeEntities[lookFor.starProjectedType]!!.addAll(closeBums)
        }
        return if (closeBums.any()) 1.0f else 0.0f
    }
}