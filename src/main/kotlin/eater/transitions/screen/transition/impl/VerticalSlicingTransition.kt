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
import com.badlogic.gdx.graphics.glutils.HdpiUtils
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import de.damios.guacamole.Preconditions
import eater.transitions.screen.transition.BatchTransition

/**
 * A transition where the new screen is sliding in in vertical slices.
 *
 * @since 0.3.0
 * @author damios
 */
class VerticalSlicingTransition @JvmOverloads constructor(
    batch: Batch, sliceCount: Int,
    duration: Float, interpolation: Interpolation? = null
) : BatchTransition(batch, duration, interpolation) {
    private val sliceCount: Int
    /**
     * @param batch
     * the sprite batch used to render
     * @param sliceCount
     * the count of slices used; has to be at least `2`
     * @param duration
     * the duration over which the transition should happen
     * @param interpolation
     * the interpolation used
     */
    /**
     * @param batch
     * the sprite batch used to render
     * @param sliceCount
     * the count of slices used; has to be at least `2`
     * @param duration
     * the duration over which the transition should happen
     */
    init {
        Preconditions.checkArgument(
            sliceCount >= 2,
            "The slice count has to be at least 2"
        )
        this.sliceCount = sliceCount
    }

    override fun render(
        delta: Float, lastScreen: TextureRegion?,
        currScreen: TextureRegion?, progress: Float
    ) {
        batch.begin()
        batch.draw(lastScreen, 0f, 0f, width.toFloat(), height.toFloat())
        val sliceWidth = MathUtils.ceil(width / sliceCount.toFloat())
        for (i in 0 until sliceCount) {
            val x = i * sliceWidth
            var offsetY = 0
            offsetY = if (i % 2 == 0) {
                (height * (progress - 1)).toInt()
            } else {
                (height * (1 - progress)).toInt()
            }
            batch.draw(
                currScreen?.texture, x.toFloat(), offsetY.toFloat(), sliceWidth.toFloat(), height.toFloat(),
                HdpiUtils.toBackBufferX(x), 0,
                HdpiUtils.toBackBufferX(sliceWidth),
                HdpiUtils.toBackBufferY(height), false, true
            )
        }
        batch.end()
    }
}