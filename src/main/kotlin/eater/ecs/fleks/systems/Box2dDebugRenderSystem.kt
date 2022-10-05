package eater.ecs.fleks.systems

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.github.quillraven.fleks.IntervalSystem

class Box2dDebugRenderSystem(
    private val box2dWorld: World,
    private val camera: OrthographicCamera
): IntervalSystem() {
    private val debugRenderer = Box2DDebugRenderer()
    override fun onTick() {
        debugRenderer.render(box2dWorld, camera.combined)
    }
}