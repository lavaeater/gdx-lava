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
package eater.transitions.screen.transition.impl

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Interpolation
import eater.transitions.screen.transition.TimedTransition

/**
 * A blank screen transition going on for a given duration.
 *
 * @since 0.3.0
 * @author damios
 */
open class BlankTimedTransition @JvmOverloads constructor(
    duration: Float,
    interpolation: Interpolation? = null
) : TimedTransition(duration, interpolation) {
    override fun render(
        delta: Float,
        lastScreen: TextureRegion?,
        currScreen: TextureRegion?,
        progress: Float
    ) {
        // do nothing
    }

    override fun create() {
        // not needed
    }

    override fun resize(width: Int, height: Int) {
        // not needed
    }

    override fun dispose() {
        // not needed
    }
}