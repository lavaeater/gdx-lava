package eater.core

import ktx.app.KtxGame
import ktx.app.KtxScreen

abstract class MainGame : KtxGame<KtxScreen>(null, false) {
    abstract fun goToGameSelect()

    abstract fun goToGameScreen()
    abstract fun goToGameOver()
    abstract fun gotoGameVictory()
}
