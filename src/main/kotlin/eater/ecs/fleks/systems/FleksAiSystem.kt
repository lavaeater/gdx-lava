package eater.ecs.fleks.systems

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import eater.ecs.fleks.components.FleksAi

class FleksAiSystem : IteratingSystem(family = World.family { all(FleksAi) }){

    override fun onTickEntity(entity: Entity) {
        val ai = entity[FleksAi]

        ai.topAction(entity)?.act(entity, deltaTime)
    }
}

