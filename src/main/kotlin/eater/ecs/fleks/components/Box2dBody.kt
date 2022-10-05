package eater.ecs.fleks.components

import com.badlogic.gdx.physics.box2d.Body
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentHook
import com.github.quillraven.fleks.ComponentType

class Box2dBody(var body: Body) : Component<Box2dBody> {
    override fun type() = Box2dBody

    companion object : ComponentType<Box2dBody>() {
        val onRemove: ComponentHook<Box2dBody> = { entity, component ->
            component.body.world.destroyBody(component.body)
            component.body.userData = null
        }
    }
}