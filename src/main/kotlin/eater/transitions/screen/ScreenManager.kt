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

import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.graphics.glutils.HdpiUtils
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.ScreenUtils
import de.damios.guacamole.Preconditions
import de.damios.guacamole.gdx.graphics.NestableFrameBuffer
import de.damios.guacamole.gdx.log.LoggerService
import de.damios.guacamole.tuple.Triple
import eater.transitions.screen.transition.ScreenTransition
import eater.transitions.utils.BasicInputMultiplexer
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * A screen manager that handles the different screens of a game and their
 * transitions.
 *
 *
 * Has to be [ initialized][.initialize] before it can be used.
 *
 *
 * Screens and transitions can be added via
 * [.addScreen] and
 * [.addScreenTransition]. To actually show a
 * screen, push it via [.pushScreen].
 *
 *
 * As the screen manager is using framebuffers internally, screens and
 * transitions have to use a [NestableFrameBuffer] if they want to use
 * framebuffers as well.
 *
 * @author damios
 *
 * @see [The
 * wiki entry detailing a screen's life-cycle](https://github.com/crykn/libgdx-screenmanager/wiki/Screen-Lifecycle)
 */
class ScreenManager<S : ManagedScreen?, T : ScreenTransition?> : Disposable {
    var LOG = LoggerService.getLogger(ScreenManager::class.java)

    /**
     * This framebuffer is used to store the content of the previously active
     * screen while a transition is played.
     */
    protected var lastFBO: FrameBuffer? = null

    /**
     * This framebuffer is used to store the content of the active screen while
     * a transition is played.
     */
    protected var currFBO: FrameBuffer? = null

    /**
     * The screen that was shown before the [current][.currScreen].
     */
    protected var lastScreen: ManagedScreen? = null
    get() = if (field === blankScreen) null else field as S?

    /**
     * The current screen.
     */
    protected var currScreen: ManagedScreen? = null

    /**
     * The input processors of the [current screen][.currScreen].
     */
    protected var currentProcessors = com.badlogic.gdx.utils.Array<InputProcessor>()

    /**
     * The blank screen used internally when no screen has been pushed yet.
     */
    private var blankScreen: BlankScreen? = null

    /**
     * The transition effect currently rendered.
     */
    protected var transition: T? = null

    /**
     * A map with all initialized screens.
     */
    private val screens: MutableMap<String, S> = ConcurrentHashMap()

    /**
     * A map with all screen transitions.
     */
    private val transitions: MutableMap<String, T> = ConcurrentHashMap()
    protected val transitionQueue: Queue<Triple<T, S, Array<Any>?>> = LinkedList()
    protected var gameInputMultiplexer: BasicInputMultiplexer? = null
    private var currentWidth = 0
    private var currentHeight = 0
    protected var initialized = false
    private var hasDepth // needed, when the framebuffers are (re)created
            = false

    fun initialize(
        gameInputMultiplexer: BasicInputMultiplexer?,
        screenWidth: Int, screenHeight: Int, hasDepth: Boolean
    ) {
        this.gameInputMultiplexer = gameInputMultiplexer
        currentWidth = screenWidth
        currentHeight = screenHeight
        this.hasDepth = hasDepth
        blankScreen = BlankScreen()
        currScreen = blankScreen
        initBuffers()
        initialized = true
    }

    protected fun initBuffers() {
        if (lastFBO != null) lastFBO!!.dispose()
        lastFBO = NestableFrameBuffer(
            Pixmap.Format.RGBA8888,
            HdpiUtils.toBackBufferX(currentWidth),
            HdpiUtils.toBackBufferY(currentHeight), hasDepth
        )
        if (currFBO != null) currFBO!!.dispose()
        currFBO = NestableFrameBuffer(
            Pixmap.Format.RGBA8888,
            HdpiUtils.toBackBufferX(currentWidth),
            HdpiUtils.toBackBufferY(currentHeight), hasDepth
        )
    }

    /**
     * Sets the `hasDepth` attribute of the internal framebuffers and
     * recreates them.
     *
     * @param hasDepth
     */
    fun setHasDepth(hasDepth: Boolean) {
        this.hasDepth = hasDepth
        initBuffers()
    }

    /**
     * Adds a screen. If a screen with the same name was added before, it is
     * replaced.
     *
     * @param name
     * the name of the screen
     * @param screen
     * the screen
     */
    fun addScreen(name: String, screen: S) {
        Preconditions.checkNotNull(screen, "screen cannot be null")
        Preconditions.checkArgument(!name.isEmpty(), "name cannot be empty")
        screens[name] = screen
    }

    /**
     * Retrieves a screen.
     *
     * @param name
     * the name of the screen
     * @return the screen
     * @throws NoSuchElementException
     * when the screen isn't found
     */
    fun getScreen(name: String): S {
        Preconditions.checkArgument(!name.isEmpty(), "name cannot be empty")
        return screens[name]
            ?: throw NoSuchElementException(
                "No screen with the name '" + name
                        + "' could be found. Add the screen via #addScreen(String, ManagedScreen) first."
            )
    }

    /**
     * @return all registered screens.
     */
    fun getScreens(): Collection<S> {
        return screens.values
    }

    /**
     * Adds a transition. If a transition with the same name was added before,
     * it is replaced.
     *
     * @param name
     * the name of the transition
     * @param transition
     * the transition
     */
    fun addScreenTransition(name: String, transition: T) {
        Preconditions.checkNotNull(transition, "screen cannot be null")
        Preconditions.checkArgument(!name.isEmpty(), "name cannot be empty")
        transitions[name] = transition
    }

    /**
     * Retrieves a transition.
     *
     * @param name
     * the name of the transition
     * @return the transition
     * @throws NoSuchElementException
     * when the transition isn't found
     */
    fun getScreenTransition(name: String): T {
        Preconditions.checkArgument(!name.isEmpty(), "name cannot be empty")
        return transitions[name]
            ?: throw NoSuchElementException(
                "No transition with the name '"
                        + name
                        + "' could be found. Add the transition via #addScreenTransition(String, ScreenTransition) first."
            )
    }

    /**
     * @return all registered transitions.
     */
    val screenTransitions: Collection<T>
        get() = transitions.values

    /**
     * Pushes a screen to be the active screen. If there is still a transition
     * ongoing, the pushed one is queued. The screen has to be added to the
     * manager beforehand via [.addScreen].
     *
     *
     * [Screen.show] is called on the pushed screen and
     * [Screen.hide] is called on the previously
     * [active screen][.getLastScreen], as soon as the transition is
     * finished. This is always done on the rendering thread (when
     * [.render] is called next).
     *
     *
     * If the same screen is pushed twice in a row, the second call is being
     * ignored.
     *
     * @param name
     * the name of screen to be pushed
     * @param transitionName
     * the transition effect; can be `null`
     * @param params
     * an array of params given to the
     * [screen][de.eskalon.commons.screen.ManagedScreen.pushParams]; can be
     * `null`
     */
    fun pushScreen(
        name: String, transitionName: String?,
        vararg params: Any
    ) {
        if (LoggerService.isDebugEnabled()) LOG.debug(
            "Screen '%s' was pushed, using the transition '%s'", name,
            transitionName ?: "null"
        )
        transitionQueue.add(
            Triple(
                transitionName?.let { getScreenTransition(it) },
                getScreen(name),
                arrayOf(*params)
            )
        )
    }

    /**
     * Renders the screens and transitions.
     *
     * @param delta
     * the time delta since the last [.render] call; in
     * seconds
     */
    fun render(delta: Float) {
        Preconditions.checkState(
            initialized,
            "The screen manager has to be initalized first!"
        )
        if (transition == null) {
            if (!transitionQueue.isEmpty()) {
                /*
				 * START THE NEXT QUEUED TRANSITION
				 */
                val nextTransition = transitionQueue.poll()
                if (nextTransition.y === currScreen) {
                    render(delta) // one can't push the same screen twice in a
                    // row
                    return
                }
                gameInputMultiplexer!!.removeProcessors(currentProcessors)
                lastScreen = currScreen
                currScreen = nextTransition.y
                currScreen!!.pushParams = if (nextTransition.z == null
                    || nextTransition.z!!.size == 0
                ) null else nextTransition.z
                currScreen!!.show()
                transition = nextTransition.x
                if (transition != null) {
                    transition!!.reset()
                } else { // a screen was pushed without transition
                    lastScreen?.hide()
                    currentProcessors = com.badlogic.gdx.utils.Array(
                        currScreen!!.inputProcessors
                    )
                    gameInputMultiplexer!!.addProcessors(currentProcessors)
                }
                render(delta) // render again so no frame is skipped
            } else {
                /*
				 * RENDER THE CURRENT SCREEN; no transition is going on
				 */
                ScreenUtils.clear(currScreen!!.clearColor, true)
                currScreen!!.render(delta)
            }
        } else {
            if (!transition!!.isDone) {
                /*
				 * RENDER THE CURRENT TRANSITION
				 */
                val lastTextureRegion = screenToTexture(
                    lastScreen, lastFBO, delta
                )
                val currTextureRegion = screenToTexture(
                    currScreen, currFBO, delta
                )
                transition!!.render(
                    delta, lastTextureRegion,
                    currTextureRegion
                )
            } else {
                /*
				 * THE CURRENT TRANSITION IS FINISHED; remove it
				 */
                transition = null
                lastScreen!!.hide()
                currentProcessors = com.badlogic.gdx.utils.Array(
                    currScreen!!.inputProcessors
                )
                gameInputMultiplexer!!.addProcessors(currentProcessors)
                render(delta) // render again so no frame is skipped
            }
        }
    }

    /**
     * @see de.eskalon.commons.screen.ManagedScreen.resize
     */
    fun resize(width: Int, height: Int) {
        Preconditions.checkState(
            initialized,
            "The screen manager has to be initalized first!"
        )
        if (currentWidth != width || currentHeight != height) {
            currentWidth = width
            currentHeight = height

            // Resize screens & transitions
            for (s in screens.values) {
                if (s!!.isInitialized) {
                    s.resize(width, height)
                }
            }
            for (t in transitions.values) {
                if (t!!.isInitialized) {
                    t.resize(width, height)
                }
            }

            // Recreate buffers
            initBuffers()
        }
    }

    /**
     * @see de.eskalon.commons.screen.ManagedScreen.pause
     */
    fun pause() {
        Preconditions.checkState(
            initialized,
            "The screen manager has to be initalized first!"
        )
        if (inTransition()) lastScreen!!.pause()
        currScreen!!.pause()
    }

    /**
     * @see de.eskalon.commons.screen.ManagedScreen.resume
     */
    fun resume() {
        Preconditions.checkState(
            initialized,
            "The screen manager has to be initalized first!"
        )
        if (inTransition()) lastScreen!!.resume()
        currScreen!!.resume()
    }

    /**
     * Disposes the screens, the transitions and the internally used
     * framebuffers.
     */
    override fun dispose() {
        lastScreen = null
        currScreen = null
        if (lastFBO != null) lastFBO!!.dispose()
        if (currFBO != null) currFBO!!.dispose()
        for (s in screens.values) {
            s!!.dispose()
        }
        for (t in transitions.values) {
            t!!.dispose()
        }
    }

    /**
     * @return the current screen; is changed in the first render pass after
     * [.pushScreen] is called.
     */
    val currentScreen: S?
        get() = if (currScreen === blankScreen) null else currScreen as S? // return null, as the blank screen is not the right
    // type
    /**
     * @return whether the manager is currently transitioning from the
     * [last screen][.getLastScreen] towards the
     * [current screen][.getCurrentScreen]
     */
    fun inTransition(): Boolean {
        return transition != null
    }

    /**
     * Renders a [screen][de.eskalon.commons.screen.ManagedScreen] into a texture region using
     * the given [framebuffer][FrameBuffer].
     *
     * @param screen
     * the screen to be rendered
     * @param fbo
     * the framebuffer the screen gets rendered into
     * @param delta
     * the time delta
     *
     * @return a texture which contains the rendered screen
     */
    fun screenToTexture(
        screen: ManagedScreen?, fbo: FrameBuffer?,
        delta: Float
    ): TextureRegion {
        fbo!!.begin()
        ScreenUtils.clear(screen!!.clearColor, true)
        screen.render(delta)
        fbo.end()
        val texture = fbo.colorBufferTexture

        // flip the texture
        val textureRegion = TextureRegion(texture)
        textureRegion.flip(false, true)
        return textureRegion
    }
}