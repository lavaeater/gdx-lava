package eater.ai

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import eater.core.engine

class GenericActionWithState<T: Component>(name: String,
                                           private val scoreFunction: (entity: Entity) -> Double,
                                           private val abortFunction: (entity: Entity) -> Unit = {},
                                           private val actFunction: (entity: Entity, state: T, deltaTime:Float) -> Unit,
                                           private val componentClass: Class<T>): AiAction(name) {
    lateinit var state: T
    val mapper = ComponentMapper.getFor(componentClass)

    override fun score(entity: Entity): Double {
        return scoreFunction(entity)
    }

    private fun setState(entity: Entity) {
        if(!mapper.has(entity)) {
            entity.add(engine().createComponent(componentClass))
        }
        state = entity.getComponent(componentClass)
    }

    private fun removeState(entity: Entity) {
        entity.remove(componentClass)
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