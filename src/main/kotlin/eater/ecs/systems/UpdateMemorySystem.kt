package eater.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import eater.ecs.components.Memory
import ktx.ashley.allOf

class UpdateMemorySystem: IteratingSystem(allOf(Memory::class).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val memory = Memory.get(entity)
        for (map in memory.closeEntities.values) {
            for((key, value) in map) {
                map[key] = value - deltaTime
                if(map[key]!! < 0f || !engine.entities.contains(key))
                    map.remove(key)
            }
        }
        for (map in memory.seenEntities.values) {
            for((key, value) in map) {
                map[key] = value - deltaTime
                if(map[key]!! < 0f || !engine.entities.contains(key))
                   map.remove(key)
            }
        }
    }
}