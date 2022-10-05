package eater.ecs.fleks.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

class CameraFollow : Component<CameraFollow> {
    override fun type() = CameraFollow

    companion object : ComponentType<CameraFollow>()
}