package eater.ui

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport

abstract class LavaHud(
    val batch: PolygonSpriteBatch,
    val hudWidth: Float = 180f,
    aspectRatio: Float = 16f / 9f) {
    val hudHeight = hudWidth * aspectRatio
    val camera = OrthographicCamera()
    val hudViewPort = ExtendViewport(hudWidth, hudHeight, camera)
    abstract val stage: Stage
    open fun render(delta: Float) {
        stage.act(delta)
        stage.draw()


    }
}