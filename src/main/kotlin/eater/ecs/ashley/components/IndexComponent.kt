package eater.ecs.ashley.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class IndexComponent: Component, Pool.Poolable {
    var index = 0
    override fun reset() {
        index = 0
    }

    companion object {
        val mapper = mapperFor<IndexComponent>()
        fun has(entity: Entity): Boolean {
            return mapper.has(entity)
        }
        fun get(entity: Entity): IndexComponent {
            return mapper.get(entity)
        }
    }
}