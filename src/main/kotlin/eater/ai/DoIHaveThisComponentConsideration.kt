package eater.ai

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.MathUtils
import kotlin.reflect.KClass

class DoIHaveThisComponentConsideration<ToCheck: Component>(name:String, toCheck: KClass<ToCheck>): Consideration(name) {
    val mapper = ComponentMapper.getFor(toCheck.java)
    override fun normalizedScore(entity: Entity): Float {
        return if(mapper.has(entity)) 1.0f else 0f
    }
}