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

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Interpolation
import de.damios.guacamole.Preconditions

/**
 * A screen transition that lasts for a certain duration.
 *
 * @author damios
 */
abstract class TimedTransition @JvmOverloads constructor(
    duration: Float,
    interpolation: Interpolation? = null
) : ScreenTransition() {
    protected var interpolation: Interpolation?
    protected var duration: Float
    protected var timePassed = 0f
    /**
     * @param duration
     * the transition's duration in seconds
     * @param interpolation
     * the interpolation to use
     *
     * @see [A
     * visual representation of the different interpolation modes](https://github.com/libgdx/libgdx/wiki/Interpolation.visual-display-of-interpolations)
     */
    /**
     * @param duration
     * the transition's duration in seconds
     */
    init {
        Preconditions.checkArgument(duration > 0)
        this.interpolation = interpolation
        this.duration = duration
    }

    override fun reset() {
        super.reset()
        timePassed = 0f
    }

    override fun render(
        delta: Float,
        lastScreen: TextureRegion?,
        currScreen: TextureRegion?
    ) {
        timePassed = timePassed + delta
        var progress = timePassed / duration
        if (interpolation != null) progress = interpolation!!.apply(progress)
        render(delta, lastScreen, currScreen, if (progress > 1f) 1f else progress)
    }

    /**
     * The render method to use in the timed transition.
     *
     * @param delta
     * @param lastScreen
     * @param currScreen
     * @param progress
     * the progress of the transition; from `0` (excl.) to
     * `1` (incl.)
     */
    abstract fun render(
        delta: Float, lastScreen: TextureRegion?,
        currScreen: TextureRegion?, progress: Float
    )

    override val isDone: Boolean
        get() = timePassed >= duration

}