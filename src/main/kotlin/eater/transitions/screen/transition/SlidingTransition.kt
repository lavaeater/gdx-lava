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

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Interpolation
import de.damios.guacamole.Preconditions
import eater.transitions.screen.transition.impl.SlidingDirection

/**
 * The base class for sliding screen transitions.
 *
 * @author damios
 *
 * @see SlidingInTransition
 *
 * @see SlidingOutTransition
 */
open class SlidingTransition(
    batch: Batch, dir: SlidingDirection,
    slideLastScreen: Boolean, duration: Float,
    interpolation: Interpolation?
) : BatchTransition(batch, duration, interpolation) {
    private val dir: SlidingDirection

    /**
     * `true` if the last screen should slide out; `false` if the
     * new screen should slide in.
     */
    private val slideLastScreen: Boolean

    init {
        Preconditions.checkNotNull(dir)
        this.dir = dir
        this.slideLastScreen = slideLastScreen
    }

    override fun render(
        delta: Float,
        lastScreen: TextureRegion?,
        currScreen: TextureRegion?,
        progress: Float
    ) {
        batch.begin()
        if (slideLastScreen) { // slide out
            batch.draw(currScreen, 0f, 0f, width.toFloat(), height.toFloat())
            batch.draw(
                lastScreen, width * dir.xPosFactor * progress,
                height * dir.yPosFactor * progress, width.toFloat(), height.toFloat()
            )
        } else { // slide in
            batch.draw(lastScreen, 0f, 0f, width.toFloat(), height.toFloat())
            batch.draw(
                currScreen, width * dir.xPosFactor * (progress - 1),
                height * dir.yPosFactor * (progress - 1), width.toFloat(), height.toFloat()
            )
        }
        batch.end()
    }
}