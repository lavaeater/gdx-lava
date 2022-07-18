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

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Interpolation
import de.damios.guacamole.Preconditions
import eater.transitions.screen.transition.BatchTransition

/**
 * A transition where the new screen is sliding in, while the last screen is
 * sliding out. Thus, the new screen is pushing the last screen out, so to
 * speak.
 *
 * @since 0.5.1
 * @author damios
 */
class PushTransition @JvmOverloads constructor(
    batch: Batch, dir: SlidingDirection,
    duration: Float, interpolation: Interpolation? = null
) : BatchTransition(batch, duration, interpolation) {
    private val dir: SlidingDirection
    /**
     * @param batch
     * the sprite batch used to render
     * @param dir
     * the direction of the push
     * @param duration
     * the duration over which the transition should happen
     * @param interpolation
     * the interpolation used
     */
    /**
     * @param batch
     * the sprite batch used to render
     * @param dir
     * the direction of the push
     * @param duration
     * the duration over which the transition should happen
     */
    init {
        Preconditions.checkNotNull(dir)
        this.dir = dir
    }

    override fun render(
        delta: Float, lastScreen: TextureRegion?,
        currScreen: TextureRegion?, progress: Float
    ) {
        batch.begin()
        batch.draw(
            currScreen, width * dir.xPosFactor * (progress - 1),
            height * dir.yPosFactor * (progress - 1), width.toFloat(), height.toFloat()
        )
        batch.draw(
            lastScreen, width * dir.xPosFactor * progress,
            height * dir.yPosFactor * progress, width.toFloat(), height.toFloat()
        )
        batch.end()
    }
}