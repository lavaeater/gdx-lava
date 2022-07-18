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
package eater.transitions.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx

/**
 * A basic game application. Takes care of setting some simple convenience
 * variables.
 *
 * @author damios
 */
open class BasicApplication : ApplicationAdapter() {
    /**
     * The width of the client area in pixels.
     *
     * @return the viewport width
     *
     * @see Graphics.getWidth
     * @see HdpiMode
     */
    /**
     * @see .getWidth
     */
    var width = 0
        protected set
    /**
     * The height of the client area in pixels.
     *
     * @return the viewport height
     *
     * @see Graphics.getHeight
     * @see HdpiMode
     */
    /**
     * @see .getHeight
     */
    var height = 0
        protected set

    override fun create() {
        width = Gdx.graphics.width
        height = Gdx.graphics.height
    }

    override fun resize(width: Int, height: Int) {
        this.width = width
        this.height = height
    }
}