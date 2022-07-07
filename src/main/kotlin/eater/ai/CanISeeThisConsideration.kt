package eater.ai

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.physics.box2d.Fixture
import eater.core.engine
import eater.core.world
import eater.ecs.components.AgentProperties
import eater.ecs.components.Memory
import eater.ecs.components.TransformComponent
import eater.physics.getEntity
import eater.physics.isEntity
import ktx.ashley.allOf
import ktx.box2d.RayCast
import ktx.box2d.rayCast
import ktx.log.debug
import ktx.math.vec2
import kotlin.reflect.KClass
import kotlin.reflect.full.starProjectedType


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