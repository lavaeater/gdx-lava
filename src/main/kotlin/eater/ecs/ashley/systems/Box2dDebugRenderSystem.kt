package eater.ecs.ashley.systems

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import eater.core.world


class Box2dDebugRenderSystem(
    private val world: World,
    private val camera: OrthographicCamera) : EntitySystem() {

    private val debugRenderer = Box2DDebugRenderer()

    override fun update(deltaTime: Float) {
        debugRenderer.render(world, camera.combined)
    }
}

class Box2dUpdateSystem(private val timeStep: Float, private val velIters: Int, private val posIters: Int): EntitySystem() {
    var accumulator = 0f
    override fun update(deltaTime: Float) {
        val ourTime = deltaTime.coerceAtMost(timeStep * 2)
        accumulator += ourTime
        while (accumulator > timeStep) {
            world().step(timeStep, velIters, posIters)
            accumulator -= ourTime
        }
    }
}

