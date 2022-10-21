package eater.ai.ashley

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import eater.core.engine
import kotlin.reflect.KClass

abstract class AiActionWithStateComponent<T: Component>(name: String, val stateComponentClass: KClass<T>): AiAction(name) {
    lateinit var state: T
    val mapper = ComponentMapper.getFor(stateComponentClass.java)
    private fun removeState(entity: Entity) {
        entity.remove(stateComponentClass.java)
    }
    private fun setState(entity: Entity) {
        if(!mapper.has(entity)) {
            val stateComponent = engine().createComponent(stateComponentClass.java)
            entity.add(stateComponent)
            initState(stateComponent)
        }
        state = entity.getComponent(stateComponentClass.java)
    }

    open fun initState(stateComponent: T) {
        //Default is of course no-op
    }

    abstract fun scoreFunction(entity: Entity):Float

    abstract fun abortFunction(entity: Entity)
    abstract fun actFunction(entity: Entity, stateComponent: T, deltaTime: Float)

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