package twodee.screens

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Stage
import twodee.core.MainGame

abstract class ScreenWithStage(mainGame: MainGame): BasicScreen(mainGame) {
    abstract val stage: Stage
    override fun render(delta: Float) {
        super.render(delta)
        stage.act(delta)
        stage.draw()
    }
}
