package eater.ai

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import eater.core.engine
import kotlin.reflect.KClass

class ConsideredActionWithState<T : Component>(
    name: String, private val abortFunction: (entity: Entity) -> Unit,
    private val actFunction: (entity: Entity, state: T, deltaTime: Float) -> Unit,
    private val componentClass: KClass<T>,
    vararg consideration: Consideration
) : AiAction(name) {
    init {
        considerations.addAll(consideration)
    }

    val mapper = ComponentMapper.getFor(componentClass.java)

    private fun ensureState(entity: Entity): T {
        if (!mapper.has(entity)) {
            entity.add(engine().createComponent(componentClass.java))
        }
        return entity.getComponent(componentClass.java)
    }


    private fun discardState(entity: Entity) {
        entity.remove(componentClass.java)
    }

    override fun abort(entity: Entity) {
        discardState(entity)
        abortFunction(entity)
    }

    override fun act(entity: Entity, deltaTime: Float) {
        actFunction(entity, ensureState(entity), deltaTime)
    }
}