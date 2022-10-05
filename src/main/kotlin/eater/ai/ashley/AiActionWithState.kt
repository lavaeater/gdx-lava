package eater.ai.ashley

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import eater.core.engine
import kotlin.reflect.KClass

abstract class AiActionWithState<T: Component>(name: String, val componentClass: KClass<T>): AiAction(name) {
    lateinit var state: T
    val mapper = ComponentMapper.getFor(componentClass.java)
    private fun removeState(entity: Entity) {
        entity.remove(componentClass.java)
    }
    private fun setState(entity: Entity) {
        if(!mapper.has(entity)) {
            entity.add(engine().createComponent(componentClass.java))
        }
        state = entity.getComponent(componentClass.java)
    }

    abstract fun scoreFunction(entity: Entity):Float

    abstract fun abortFunction(entity: Entity)
    abstract fun actFunction(entity: Entity, state: T, deltaTime: Float)

    override fun abort(entity: Entity) {
        removeState(entity)
        abortFunction(entity)
    }

    override fun act(entity: Entity, deltaTime: Float) {
        setState(entity)
        actFunction(entity, state, deltaTime)
    }

    override fun updateScore(entity: Entity): Float {
        score = scoreFunction(entity)
        return score
    }
}