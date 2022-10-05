package eater.ecs.ashley.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import eater.ai.ashley.AiComponent
import ktx.ashley.allOf
import ktx.ashley.mapperFor

class AshleyAiSystem : IteratingSystem(allOf(AiComponent::class).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val ai = AiComponent.get(entity)
        ai.topAction(entity)?.act(entity, deltaTime)
    }
}

