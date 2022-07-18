/*
 * Copyright 2020 damios
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 * http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eater.transitions.game

import com.badlogic.gdx.Gdx
import eater.transitions.screen.ManagedScreen
import eater.transitions.screen.ScreenManager
import eater.transitions.screen.transition.ScreenTransition
import eater.transitions.utils.BasicInputMultiplexer

/**
 * A game class that utilizes a [screen manager][ScreenManager]. Use
 * the [provided instance][.getScreenManager] to
 * [register screens][ScreenManager.addScreen]
 * and [ transitions][ScreenManager.addScreenTransition], as well as to
 * [push those][ScreenManager.pushScreen].
 *
 *
 * Input listeners have to be added via the game's [ input multiplexer][.inputProcessor].
 *
 * @author damios
 */
open class ManagedGame<S : ManagedScreen?, T : ScreenTransition?> : BasicApplication() {
    /**
     * Returns the input multiplexer of the game. Must be used to add input
     * listeners instead of [Input.setInputProcessor].
     *
     * @return the game's input multiplexer
     */
    /**
     * The input multiplexer of the game. Must (!) be used to add input
     * listeners instead of [Input.setInputProcessor].
     */
    val inputMultiplexer = BasicInputMultiplexer()

    /**
     * The game's screen manager. Is used to register and push new
     * screens/transitions.
     */
    var screenManager: ScreenManager<S, T>
        protected set

    init {
        screenManager = ScreenManager()
    }

    override fun create() {
        super.create()
        Gdx.input.inputProcessor = inputMultiplexer
        screenManager.initialize(
            inputMultiplexer, width, height,
            false
        )
    }

    override fun render() {
        screenManager.render(Gdx.graphics.deltaTime)
    }

    /**
     * Called when the [Application] is resized. This can happen at any
     * point during a non-paused state, but will never happen before a call to
     * [.create].
     *
     *
     * `resize(0, 0)` calls, which may happen when the game is minimized
     * on Windows, are ignored.
     *
     * @param width
     * the new width in pixels
     * @param height
     * the new height in pixels
     */
    override fun resize(width: Int, height: Int) {
        if (width == 0 || height == 0) // if the game is minimized on Windows,
        // resize(0, 0) is called. This causes
        // problems, as a framebuffer with these
        // dimensions cannot be created.
        // Therefore, it is simply ignored.
            return
        super.resize(width, height)
        screenManager.resize(width, height)
    }

    override fun pause() {
        screenManager.pause()
    }

    override fun resume() {
        screenManager.resume()
    }

    override fun dispose() {
        screenManager.dispose()
    }
}