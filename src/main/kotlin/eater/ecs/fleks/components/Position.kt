package eater.ecs.fleks.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Position(var x: Float, var y: Float) : Component<Position> {
    override fun type(): ComponentType<Position> = Position

    companion object : ComponentType<Position>()
}

