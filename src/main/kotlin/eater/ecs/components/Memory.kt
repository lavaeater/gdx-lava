package eater.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor
import kotlin.reflect.KType

class Memory : Component, Pool.Poolable {
    val seenEntities = mutableMapOf<KType, MutableMap<Entity, Float>>()
    val closeEntities = mutableMapOf<KType, MutableMap<Entity, Float>>()
    val memoryLifeSpan = 5f
    override fun reset() {
        seenEntities.clear()
        closeEntities.clear()
    }

    companion object {
        val mapper = mapperFor<Memory>()
        fun has(entity: Entity): Boolean {
            return mapper.has(entity)
        }

        fun get(entity: Entity): Memory {
            return mapper.get(entity)
        }
    }
}