package eater.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import eater.ai.AiComponent
import ktx.ashley.allOf

class UpdateActionsSystem : IteratingSystem(allOf(AiComponent::class).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val ai = AiComponent.get(entity)
        ai.updateAction(entity)
    }

}