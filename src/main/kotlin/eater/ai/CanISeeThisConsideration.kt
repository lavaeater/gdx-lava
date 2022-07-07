package eater.ai

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.utils.Pool.Poolable
import eater.core.engine
import eater.core.world
import eater.ecs.components.AgentProperties
import eater.ecs.components.TransformComponent
import ktx.ashley.allOf
import ktx.box2d.RayCast
import ktx.box2d.rayCast
import ktx.log.debug
import ktx.math.vec2
import eater.physics.getEntity
import eater.physics.isEntity
import ktx.ashley.mapperFor
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.starProjectedType


fun canISeeYouFromHere(from: Vector2, aimVector: Vector2, to: Vector2, fovDeg: Float): Boolean {
    return angleToDeg(from, aimVector, to) < fovDeg / 2
}

fun angleToDeg(from: Vector2, aimVector: Vector2, to: Vector2): Float {
    return MathUtils.acos(
        aimVector.dot(to.cpy().sub(from).nor())
    ) * MathUtils.radiansToDegrees
}

class Memory : Component, Poolable {
    val seenEntities = mutableMapOf<KType, MutableList<Entity>>()
    val closeEntities = mutableMapOf<KType, MutableList<Entity>>()
    override fun reset() {
        seenEntities.clear()
    }

    companion object {
        val mapper = mapperFor<Memory>()
        fun has(entity: Entity): Boolean {
            return mapper.has(entity)
        }

        fun get(entity: Entity): Memory {
            return mapper.get(entity)
        }
    }
}

class AmICloseToThisConsideration<ToLookFor : Component, ToStoreIn : Memory>(
    private val lookFor: KClass<ToLookFor>,
    private val toStoreIn: KClass<ToStoreIn>,
    private val distance: Float
) : Consideration("Am I Close to This?") {
    private val entitiesToLookForFamily = allOf(lookFor, TransformComponent::class).get()
    private val engine by lazy { engine() }
    override fun normalizedScore(entity: Entity): Float {
        val position = TransformComponent.get(entity).position
        val closeBums = engine.getEntitiesFor(entitiesToLookForFamily)
            .filter { TransformComponent.get(it).position.dst(position) < distance }
        if(closeBums.any()) {
            val memory = ensureMemory(entity, lookFor, toStoreIn)
            if (!memory.closeEntities.containsKey(lookFor.starProjectedType)) {
                memory.closeEntities[lookFor.starProjectedType] = mutableListOf()
            } else {
                memory.closeEntities[lookFor.starProjectedType]!!.clear()
            }
            memory.closeEntities[lookFor.starProjectedType]!!.addAll(closeBums)
        }
        return if (closeBums.any()) 1.0f else 0.0f
    }
}

class CanISeeThisConsideration<ToLookFor : Component, ToStoreIn : Memory>(
    private val lookFor: KClass<ToLookFor>,
    private val toStoreIn: KClass<ToStoreIn>,
    private val stop: Boolean = false
) : Consideration("Can I See ") {
    private val storeMapper = ComponentMapper.getFor(toStoreIn.java)
    private val entitiesToLookForFamily = allOf(lookFor, TransformComponent::class).get()
    private val engine by lazy { engine() }
    override fun normalizedScore(entity: Entity): Float {
        val agentProps = AgentProperties.get(entity)
        val memory = ensureMemory(entity, lookFor, toStoreIn)
        if (!memory.seenEntities.containsKey(lookFor.starProjectedType)) {
            memory.seenEntities[lookFor.starProjectedType] = mutableListOf()
        } else {
            memory.seenEntities[lookFor.starProjectedType]!!.clear()
        }
        val seenEntities = memory.seenEntities[lookFor.starProjectedType]!!
        if (stop) {
            agentProps.speed = 0f
        }
        val agentPosition = TransformComponent.get(entity).position
        val inRange = engine.getEntitiesFor(entitiesToLookForFamily)
            .filter { TransformComponent.get(it).position.dst(agentPosition) < agentProps.viewDistance }
            .filter {
                canISeeYouFromHere(
                    agentPosition,
                    agentProps.directionVector,
                    TransformComponent.get(it).position,
                    agentProps.fieldOfView
                )
            }
        debug { "LookForAndStore found ${inRange.size} entities in range and in the field of view" }
        var haveIseenSomething = false
        for (potential in inRange) {
            val entityPosition = TransformComponent.get(potential).position
            var lowestFraction = 1f
            var closestFixture: Fixture? = null
            val pointOfHit = vec2()
            val hitNormal = vec2()


            world().rayCast(
                agentPosition,
                entityPosition
            ) { fixture, point, normal, fraction ->
                if (fraction < lowestFraction) {
                    lowestFraction = fraction
                    closestFixture = fixture
                    pointOfHit.set(point)
                    hitNormal.set(normal)
                }
                RayCast.CONTINUE
            }

            if (closestFixture != null && closestFixture!!.isEntity() && inRange.contains(closestFixture!!.getEntity())) {
                debug { "LookForAndStore - entity at $entityPosition can be seen " }
                seenEntities.add(closestFixture!!.getEntity())
                haveIseenSomething = true
//                break
            }
        }
        return if (haveIseenSomething) 0.95f else 0f
    }
}

fun <ToLookFor : Component, ToStoreIn : Memory> ensureMemory(
    entity: Entity,
    lookFor: KClass<ToLookFor>,
    toStoreIn: KClass<ToStoreIn>
): ToStoreIn {
    val mapper = ComponentMapper.getFor(toStoreIn.java)
    val memory = if (!mapper.has(entity)) {
        val component = engine().createComponent(toStoreIn.java)
        entity.add(component)
        component
    } else {
        mapper.get(entity)
    }
    return memory
}