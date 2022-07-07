package eater.ai

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import kotlin.reflect.KClass

class DoIHaveThisComponentConsideration<ToCheck: Component>(name:String, toCheck:KClass<ToCheck>): Consideration(name) {
    val mapper = ComponentMapper.getFor(toCheck.java)
    override fun normalizedScore(entity: Entity): Float {
        return if(mapper.has(entity)) 1.0f else 0f
    }
}

class InvertedConsideration(name: String, consideration: Consideration):Consideration(name, {entity-> 1f/consideration.scoreFunction(entity) })

open class Consideration(val name: String, val scoreFunction: (entity: Entity) -> Float = { 0f }) {
    open fun normalizedScore(entity: Entity): Float {
        return scoreFunction(entity)
    }

//    object MyHealthConsideration
}