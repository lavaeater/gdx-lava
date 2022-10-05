package eater.ecs.ashley.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor
import ktx.math.vec2


class TransformComponent : Component, Pool.Poolable {
    var feelsGravity = false
    val position: Vector2 = vec2()
    var height = 4f
    var verticalSpeed = 0f
    var rotation = 0f

    override fun reset() {
        position.set(Vector2.Zero)
        rotation = 0f
        height = 4f
        verticalSpeed = 0f
    }

    companion object {
        val mapper = mapperFor<TransformComponent>()
        fun get(entity: Entity): TransformComponent {
            return mapper.get(entity)
        }

        fun has(entity: Entity): Boolean {
            return mapper.has(entity)
        }
    }
}

