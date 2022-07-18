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
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
import de.damios.guacamole.Preconditions
import eater.transitions.screen.transition.impl.BlankTimedTransition

/**
 * The base class for all transitions using a [Batch].
 *
 * @author damios
 */
abstract class BatchTransition(
    batch: Batch, duration: Float,
    interpolation: Interpolation?
) : BlankTimedTransition(duration, interpolation) {
    protected var batch: Batch
    protected var viewport: Viewport
    protected var width = 0
    protected var height = 0

    /**
     * @param batch
     * the batch used for rendering the transition. If it is used
     * outside of the transitions, don't forget to set the project
     * matrix!
     * @param duration
     * @param interpolation
     */
    init {
        Preconditions.checkNotNull(batch)
        this.batch = batch
        viewport = ScreenViewport() // Takes care of rendering the
        // transition over the whole
        // screen
    }

    override fun render(
        delta: Float,
        lastScreen: TextureRegion?,
        currScreen: TextureRegion?
    ) {
        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined
        super.render(delta, lastScreen, currScreen)
    }

    override fun resize(width: Int, height: Int) {
        this.width = width
        this.height = height
        viewport.update(width, height, true)
    }
}