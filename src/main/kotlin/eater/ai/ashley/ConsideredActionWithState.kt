package eater.ai.ashley

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import eater.core.engine
import kotlin.reflect.KClass

/**
 * This action will, in the act function, add a component
 * to the agent. if the agent already has this component
 * the score function will return 0f.
 * However, if the terms for the score are no longer upheld,
 * the component shall be removed.
 *
 * Something like that.
 */
class AddComponentIfConsiderationIsTrueAction<ToAdd: Component>(name: String, scoreRange: ClosedFloatingPointRange<Float>, private val componentToAdd: KClass<ToAdd>, vararg consideration: Consideration) : AiAction(name, scoreRange) {
    init {
        considerations.addAll(consideration)
    }
    val mapper = ComponentMapper.getFor(componentToAdd.java)!!
    var abortFunction: (entity: Entity) -> Unit = {}
    var initStateFunction: (ToAdd)->Unit = {}
    private fun ensureComponent(entity: Entity): ToAdd {
        if(!mapper.has(entity)) {
            val stateComponent = engine().createComponent(componentToAdd.java)
            entity.add(stateComponent)
        }
        return entity.getComponent(componentToAdd.java)
    }

    private fun discardState(entity: Entity) {
        entity.remove(componentToAdd.java)
    }

    override fun abort(entity: Entity) {
        discardState(entity)
        abortFunction(entity)
    }

    override fun act(entity: Entity, deltaTime: Float) {
        /**
         * The motherfucking can i see this consideration adds seen entitities
         * to the motherfucking memory of the entity which is so fudging cool it isn't even cool.
         * No, it is cool, actually.
         *
         * But it uses agent properties, which means I have two sets of properties now. I have
         * to merge this into one component.
         */
    }
}

class ConsideredActionWithState<T : Component>(
    name: String,
    private val actFunction: (entity: Entity, stateComponent: T, deltaTime: Float) -> Unit,
    private val stateComponentClass: KClass<T>,
    scoreRange: ClosedFloatingPointRange<Float> = 0f..1f,
    vararg consideration: Consideration
) : AiAction(name, scoreRange) {
    init {
        considerations.addAll(consideration)
    }

    val mapper = ComponentMapper.getFor(stateComponentClass.java)!!
    var abortFunction: (entity: Entity) -> Unit = {}
    var initStateFunction: (T)->Unit = {}

    private fun ensureState(entity: Entity): T {
        if(!mapper.has(entity)) {
            val stateComponent = engine().createComponent(stateComponentClass.java)
            entity.add(stateComponent)
            initState(stateComponent)
        }
        return entity.getComponent(stateComponentClass.java)
    }
    private fun initState(stateComponent: T) {
        initStateFunction(stateComponent)
    }


    private fun discardState(entity: Entity) {
        entity.remove(stateComponentClass.java)
    }

    override fun abort(entity: Entity) {
        discardState(entity)
        abortFunction(entity)
    }

    override fun act(entity: Entity, deltaTime: Float) {
        actFunction(entity, ensureState(entity), deltaTime)
    }
}