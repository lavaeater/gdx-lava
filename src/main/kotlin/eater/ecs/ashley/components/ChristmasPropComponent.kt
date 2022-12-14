package eater.ecs.ashley.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class ChristmasPropComponent: Component, Pool.Poolable {
    val props = mutableMapOf<PropName, CoolProp>()

    fun getProp(propName: PropName): CoolProp {
        return props[propName]!!
    }

    override fun reset() {
        props.clear()
    }

    companion object {
        val mapper = mapperFor<ChristmasPropComponent>()
        fun has(entity: Entity): Boolean {
            return mapper.has(entity)
        }
        fun get(entity: Entity): ChristmasPropComponent {
            return mapper.get(entity)
        }
    }
}