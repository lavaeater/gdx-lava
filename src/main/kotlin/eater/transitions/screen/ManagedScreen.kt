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
package eater.transitions.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color

/**
 * A basic screen for use with a [ScreenManager]. Screens are intended as
 * objects that are created once (when the game is started) and are then reused.
 *
 *
 * A screen starts rendering after it has been
 * [pushed][ScreenManager.pushScreen].
 * Every time a screen is pushed [.show] is called before the rendering
 * commences, allowing you to (re)set the screen's state.
 *
 *
 * When the screen is shown for the first time, in addition to [.show]
 * the [.create] method is called. This method is intended to be used to
 * load any assets the screen may need or to setup the screen's UI. The screen
 * can also be [initialized][.create] manually by calling
 * [.initializeScreen], which should normally be done by a loading
 * screen after the game's assets have been loaded centrally.
 *
 *
 * Use [.addInputProcessor] to add input processors that
 * are automatically registered and unregistered whenever the screen is
 * [shown][.show]/[hidden][.hide].
 *
 * @author damios
 *
 * @see [The
 * wiki entry detailing a screen's life-cycle](https://github.com/crykn/libgdx-screenmanager/wiki/Screen-Lifecycle)
 *
 * @see [An
 * example lifecycle for a screen](https://github.com/crykn/libgdx-screenmanager/wiki/Lifecycle-Example)
 */
abstract class ManagedScreen : Screen {
    /**
     * @see .addInputProcessor
     */
    internal val inputProcessors: com.badlogic.gdx.utils.Array<InputProcessor> =
        com.badlogic.gdx.utils.Array<InputProcessor>(4)

    /**
     * Contains any arguments given to
     * [ScreenManager.pushScreen]. Is
     * `null` if no arguments were provided.
     *
     *
     * These params are usually accessed in [.show].
     */
    internal var pushParams: Array<Any>? = null
    var isInitialized = false
        private set

    /**
     * Can be called manually to [initialize][.create] the screen -
     * otherwise this is done when the screen is first [ shown][.show].
     */
    fun initializeScreen() {
        if (!this.isInitialized) {
            this.isInitialized = true
            create()
            resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())
        }
    }

    /**
     * Is responsible for initializing the screen. Is called *once*.
     *
     *
     * Right after this method [.resize] is called.
     */
    protected abstract fun create()

    /**
     * Adds an input processor that is automatically registered and unregistered
     * whenever the screen is [shown][.show]/[ hidden][.hide].
     *
     * @param processor
     * the processor to add
     */
    protected fun addInputProcessor(processor: InputProcessor) {
        inputProcessors.add(processor)
    }

    /**
     * Called when this screen becomes the
     * [active screen][ScreenManager.getCurrentScreen]. At first,
     * the screen may be rendered as part of a transition.
     *
     *
     * If the [ScreenManager.pushScreen] call
     * had any params set, those can be found in [.pushParams].
     */
    override fun show() {
        initializeScreen()
    }

    /**
     * Called when this screen is no longer the
     * [active screen][ScreenManager.getCurrentScreen] for a
     * [ManagedGame] and a possible transition has finished.
     */
    abstract override fun hide()

    /**
     * Called when the screen should render itself.
     *
     *
     * Before this method is called, the previously rendered stuff is cleared
     * with the [clear color][.getClearColor].
     *
     *
     * If you are using any [Viewport]s, be sure to
     * [apply][Viewport.apply] them first. When using the same
     * [Batch] as the transitions, don't forget to
     * [ set the projection matrix][Batch.setProjectionMatrix] before using it. For example:
     *
     * <pre>
     * `viewport.apply();
     * Batch.setProjectionMatrix(viewport.getCamera().combined);
     *
     * // And then render your stuff:
     * Batch.begin();
     * // ...
     * Batch.end();
    ` *
    </pre> *
     *
     * @param delta
     * the time in seconds since the last render pass
     */
    abstract override fun render(delta: Float)

    /**
     * Called when the [game is][ApplicationListener.resize], the screen was [initialized][.isInitialized] before
     * and the new size is different to the previous one.
     *
     *
     * In addition, this method is called once right after the screen was
     * initialized ([.create]).
     *
     * @param width
     * the new width in pixels
     * @param height
     * the new height in pixels
     */
    abstract override fun resize(width: Int, height: Int)

    /**
     * Called when the [Application] is paused while this screen is
     * rendered.
     *
     *
     * The game is usually paused when it is not active or visible on-screen.
     * <u>On desktop</u> this is the case, when the game is minimized. <u>On
     * Android</u> this method is called when the home button is pressed or an
     * incoming call is received.
     *
     * @see .resume
     */
    override fun pause() {}

    /**
     * Called when the [Application] is resumed from a paused state;
     * usually when it regains focus.
     *
     *
     * <u>On Android</u> the OpenGL context is lost on pause. In general, libGDX
     * will re-create OpenGL objects that were lost, but if, for example, there
     * are any run-time created textures, they will have to re-created in this
     * method.
     *
     * @see .pause
     * @see [Some
     * information on the android lifecycle](http://bitiotic.com/blog/2013/05/23/libgdx-and-android-application-lifecycle/)
     */
    override fun resume() {}

    /**
     * {@inheritDoc}
     *
     * Is automatically called when the game is closed, without regard as to
     * whether the screen was [initialized][.isInitialized] before.
     */
    abstract override fun dispose()
    fun getInputProcessors(): com.badlogic.gdx.utils.Array<InputProcessor> {
        return inputProcessors
    }

    /**
     * @return the color to clear the screen with before the rendering is
     * started
     */
    val clearColor: Color
        get() = Color.BLACK
}