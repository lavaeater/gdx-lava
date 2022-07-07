package eater.ai

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Fixture
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
import kotlin.reflect.KClass


fun canISeeYouFromHere(from: Vector2, aimVector: Vector2, to: Vector2, fovDeg: Float): Boolean {
    return angleToDeg(from, aimVector, to) < fovDeg / 2
}

fun angleToDeg(from: Vector2, aimVector: Vector2, to: Vector2) : Float {
    return MathUtils.acos(
        aimVector.dot(to.cpy().sub(from).nor())) * MathUtils.radiansToDegrees
}

class CanISeeThisConsideration<ToLookFor : Component>(
    componentClass: KClass<ToLookFor>,
    private val stop: Boolean = false
) : Consideration("Can I See ") {
    private val entitiesToLookForFamily = allOf(componentClass, TransformComponent::class).get()
    private val engine by lazy { engine() }
    override fun normalizedScore(entity: Entity): Float {
        val agentProps = AgentProperties.get(entity)
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
                haveIseenSomething = true
                break
            }
        }
        return if (haveIseenSomething) 1.0f else 0f
    }
}