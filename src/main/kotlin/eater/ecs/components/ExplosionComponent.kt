package eater.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool.Poolable
import ktx.ashley.mapperFor
import ktx.math.vec2

data class ExplosionBlorb(val position: Vector2, val startRadius: Float = 1f, val endRadius: Float = 15f, val backColor: Color = Color(1f, 0f, 0f, 0.5f), val frontColor: Color = Color(0.7f, 0.4f, 0f, 0.5f)) {
    var currentRadius = startRadius
}

class ExplosionComponent: Component, Poolable {
    var explosionTime = 0.1f
    val explosionBlorbs = mutableListOf<ExplosionBlorb>().apply {
        addAll(getBlorbs())
    }

    fun getBlorbs():Array<ExplosionBlorb> {
        val posRange = (-25..25)
        val blorbRange = 3..15
        return Array(blorbRange.random()) {
            ExplosionBlorb(vec2(posRange.random() / 10f, posRange.random() / 10f))
        }
    }

    fun setBlorbs() {
        explosionBlorbs.clear()
        explosionBlorbs.addAll(getBlorbs())
    }


    override fun reset() {
        setBlorbs()
    }

    companion object {
        private val mapper = mapperFor<ExplosionComponent>()
        fun has(entity: Entity): Boolean {
            return mapper.has(entity)
        }

        fun get(entity: Entity): ExplosionComponent {
            return mapper.get(entity)
        }
    }
}