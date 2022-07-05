package eater.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import eater.ai.AiComponent
import ktx.ashley.allOf

class UpdateActionsSystem : IntervalIteratingSystem(allOf(AiComponent::class).get(), 1f) {
    override fun processEntity(entity: Entity) {
        val ai = AiComponent.get(entity)
        ai.updateAction(entity)
    }

}