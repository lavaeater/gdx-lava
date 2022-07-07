package eater.ai

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.MathUtils
import kotlin.reflect.KClass

class DoIHaveThisComponentConsideration<ToCheck: Component>(name:String, toCheck: KClass<ToCheck>, scoreRange: ClosedFloatingPointRange<Float> = 0f..1f): Consideration(name, scoreRange = scoreRange) {
    val mapper = ComponentMapper.getFor(toCheck.java)
    override fun normalizedScore(entity: Entity): Float {
        return MathUtils.map(0f, 1f, scoreRange.start, scoreRange.endInclusive,if(mapper.has(entity)) 1.0f else 0f)
    }
}