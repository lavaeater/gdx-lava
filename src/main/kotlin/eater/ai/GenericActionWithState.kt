package eater.ai

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import eater.core.engine
import kotlin.reflect.KClass

class GenericActionWithState<T: Component>(name: String,
                                           private val scoreFunction: (entity: Entity, state: T) -> Float,
                                           private val abortFunction: (entity: Entity) -> Unit = {},
                                           private val actFunction: (entity: Entity, state: T, deltaTime:Float) -> Unit,
                                           private val componentClass: KClass<T>): AiAction(name) {
    lateinit var state: T
    val mapper = ComponentMapper.getFor(componentClass.java)

    override fun updateScore(entity: Entity): Float {
        setState(entity)
        score = scoreFunction(entity, state)
        return score
    }

    private fun setState(entity: Entity) {
        if(!mapper.has(entity)) {
            entity.add(engine().createComponent(componentClass.java))
        }
        state = entity.getComponent(componentClass.java)
    }

    private fun removeState(entity: Entity) {
        entity.remove(componentClass.java)
    }

    override fun abort(entity: Entity) {
        removeState(entity)
        abortFunction(entity)
    }

    override fun act(entity: Entity, deltaTime: Float) {
        setState(entity)
        actFunction(entity, state, deltaTime)
    }
}