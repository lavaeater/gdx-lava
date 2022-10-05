package eater.ecs.fleks.components

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Sprite(var textureRegion: TextureRegion) : Component<Sprite> {
    override fun type() = Sprite

    companion object : ComponentType<Sprite>()
}