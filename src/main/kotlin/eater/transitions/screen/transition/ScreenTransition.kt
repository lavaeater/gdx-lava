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
package eater.transitions.screen.transition

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Disposable

/**
 * A transition effect between two screen for use with a [ScreenManager].
 * Transitions are intended as objects that are created only once when the game
 * is started and are then reused.
 *
 *
 * The [.create] method is called when the transition is first used. The
 * transition can also be initialized manually by calling
 * [.initializeScreenTransition], which should normally be done by a
 * loading screen after the assets have been loaded.
 *
 * @author damios
 *
 * @see ScreenManager.pushScreen
 */
abstract class ScreenTransition : Disposable {
    var isInitialized = false
        private set

    /**
     * Can be called manually to [initialize][.create] the
     * transition - otherwise this is done when the transition is first
     * rendered.
     */
    fun initializeScreenTransition() {
        if (!this.isInitialized) {
            this.isInitialized = true
            create()
            resize(Gdx.graphics.width, Gdx.graphics.height)
        }
    }

    /**
     * Is responsible for initializing the transition. Is called *once*.
     *
     *
     * Right after this method [.resize] is called.
     */
    protected abstract fun create()

    /**
     * Takes care of actually rendering the transition.
     *
     * @param delta
     * the time delta
     * @param lastScreen
     * the old screen as a texture region
     * @param currScreen
     * the screen the manager is transitioning to as a texture region
     */
    abstract fun render(
        delta: Float,
        lastScreen: TextureRegion?,
        currScreen: TextureRegion?
    )

    /**
     * @return whether the transition is done
     */
    abstract val isDone: Boolean

    /**
     * Called when the [game is][ApplicationListener.resize], the transition was [initialized][.isInitialized]
     * before and the new size is different to the previous one.
     *
     *
     * In addition, this method is called once right after the transition was
     * initialized ([.create]).
     *
     * @param width
     * the new width in pixels
     * @param height
     * the new height in pixels
     */
    abstract fun resize(width: Int, height: Int)

    /**
     * Is called to reset the transition for another use.
     *
     * This is done right before the transition is to be used.
     */
    open fun reset() {
        initializeScreenTransition()
    }
}