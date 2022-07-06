package eater.ai

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import eater.core.engine

class ConsideredActionWithState<T: Component>(name: String, private val abortFunction: (entity: Entity) -> Unit,
                                              private val actFunction: (entity: Entity, deltaTime:Float) -> Unit,
                                              private val componentClass: Class<T>,
                                              vararg consideration: Consideration
): AiAction(name) {
    init {
        considerations.addAll(consideration)
    }

    lateinit var state: T
    val mapper = ComponentMapper.getFor(componentClass)

    private fun ensureState(entity: Entity) {
        if(!mapper.has(entity)) {
            entity.add(engine().createComponent(componentClass))
        }
        state = entity.getComponent(componentClass)
    }


    private fun discardState(entity: Entity) {
        entity.remove(componentClass)
    }
    override fun abort(entity: Entity) {
        discardState(entity)
        abortFunction(entity)
    }

    override fun act(entity: Entity, deltaTime: Float) {
        ensureState(entity)
        actFunction(entity, deltaTime)
    }
}