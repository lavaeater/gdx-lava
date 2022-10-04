package eater.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import eater.ai.ashley.AiComponent
import ktx.ashley.allOf
import ktx.ashley.mapperFor

class UtilityAiSystem : IteratingSystem(allOf(AiComponent::class).get()) {
    private val utilMapper = mapperFor<AiComponent>()
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val ai = utilMapper.get(entity)
        ai.topAction(entity)?.act(entity, deltaTime)
    }
}

