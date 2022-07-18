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
import eater.transitions.screen.transition.BatchTransition

/**
 * A transition that blends two screens together over a certain time interval.
 *
 * @since 0.3.0
 * @author damios
 */
class BlendingTransition @JvmOverloads constructor(
    batch: Batch, duration: Float,
    interpolation: Interpolation? = null
) : BatchTransition(batch, duration, interpolation) {
    override fun render(
        delta: Float, lastScreen: TextureRegion?,
        currScreen: TextureRegion?, progress: Float
    ) {
        batch.begin()

        // Blends the two screens
        val c = batch.color
        batch.draw(lastScreen, 0f, 0f, width.toFloat(), height.toFloat())
        batch.setColor(c.r, c.g, c.b, progress)
        batch.draw(currScreen, 0f, 0f, width.toFloat(), height.toFloat())
        batch.setColor(c.r, c.g, c.b, 1f)
        batch.end()
    }
}