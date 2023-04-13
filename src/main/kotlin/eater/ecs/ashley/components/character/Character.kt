package eater.ecs.ashley.components.character

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.Pool.Poolable
import ktx.math.plus
import ktx.math.times
import ktx.math.vec2
import eater.input.CardinalDirection

class Character: Component, Poolable {
    var width: Float = 32f
    var height: Float = 32f
    var scale: Float = 1.0f
    val direction = Direction()
    val worldPosition get() = direction.worldPosition
    val forward = direction.forward

    var angleDegrees get() = direction.angleDegrees
    set(value) {
            direction.angleDegrees = value
        }
    val cardinalDirection get() = direction.cardinalDirection

    private val anchors = mapOf(
        CardinalDirection.East to
                mapOf(
                    "rightshoulder" to vec2(-0.25f, 0.5f),
                    "leftshoulder" to vec2(0f, -0.25f)
                ),
        CardinalDirection.South to
                mapOf(
                    "rightshoulder" to vec2(0.25f, 0.5f),
                    "leftshoulder" to vec2(0.25f, -0.5f)
                ),
        CardinalDirection.West to
                mapOf(
                    "rightshoulder" to vec2(0f, 0.25f),
                    "leftshoulder" to vec2(-0.25f, -0.5f)
                ),
        CardinalDirection.North to
                mapOf(
                    "rightshoulder" to vec2(-0.25f, 0.5f),
                    "leftshoulder" to vec2(-0.25f, -0.5f)
                )
    )

    val aimVector: Vector2 = Vector2.X.cpy()

    val worldAnchors
        get() = anchors[cardinalDirection]!!.map {
            it.key to worldPosition + vec2().set(it.value.x, it.value.y * 0.5f).times(32f)
                .setAngleDeg(direction.cardinalAngle - it.value.angleDeg())
        }.toMap()

    override fun reset() {
        worldPosition.setZero()
        angleDegrees = 0f
        aimVector.set(1f,0f)
        width = 32f
        height = 32f
        scale = 1.0f
    }

}