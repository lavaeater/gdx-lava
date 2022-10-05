package eater.ai.ashley

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.physics.box2d.Fixture
import eater.core.engine
import eater.core.world
import eater.ecs.ashley.components.AgentProperties
import eater.ecs.ashley.components.Memory
import eater.ecs.ashley.components.TransformComponent
import eater.physics.createComponent
import eater.physics.getEntity
import eater.physics.isEntity
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.box2d.RayCast
import ktx.box2d.rayCast
import ktx.log.debug
import ktx.math.vec2
import kotlin.reflect.KClass
import kotlin.reflect.full.starProjectedType


class CanISeeThisConsideration<ToLookFor : Component>(
    private val lookFor: KClass<ToLookFor>
) : Consideration("Can I See ") {
    private val storeMapper = mapperFor<Memory>()
    private val entitiesToLookForFamily = allOf(lookFor, TransformComponent::class).get()
    private val engine by lazy { engine() }
    override fun normalizedScore(entity: Entity): Float {
        val agentProps = AgentProperties.get(entity)
        val memory = ensureMemory(entity)
        if (!memory.seenEntities.containsKey(lookFor.starProjectedType)) {
            debug { "Create memory for ${lookFor.starProjectedType}" }
            memory.seenEntities[lookFor.starProjectedType] = mutableMapOf()
        }
        val seenEntities = memory.seenEntities[lookFor.starProjectedType]!!
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
        debug { "CanISeeThisConsideration found ${inRange.size} entities in range and in the field of view" }
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
                debug { "CanISeeThisConsideration - entity at $entityPosition can be seen" }
                val e = closestFixture!!.getEntity()
                seenEntities[e] = memory.memoryLifeSpan
            }
        }
        return if (seenEntities.any() ) 1f else 0f
    }
}

fun ensureMemory(
    entity: Entity
): Memory {
    val mapper = mapperFor<Memory>()
    val memory = if (!mapper.has(entity)) {
        val component = engine().createComponent<Memory>()
        entity.add(component)
        component
    } else {
        mapper.get(entity)
    }
    return memory
}