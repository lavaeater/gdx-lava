package eater.ecs.ashley.systems


import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.OrthographicCamera
import eater.ecs.ashley.components.Box2d
import eater.ecs.ashley.components.CameraFollow
import ktx.ashley.allOf
import ktx.math.vec2
import ktx.math.vec3

open class CameraFollowSystem(
    protected val camera: OrthographicCamera,
    protected val alpha: Float
) :
    IteratingSystem(
        allOf(
            CameraFollow::class,
            Box2d::class
        ).get()
    ) {

    protected val cameraPosition = vec2()


    override fun processEntity(entity: Entity, deltaTime: Float) {

        val body = Box2d.get(entity).body
        cameraPosition.set(body.position)

        camera.position.lerp(
            vec3(cameraPosition, 0f), alpha
        )

        camera.update(true)



    }
}