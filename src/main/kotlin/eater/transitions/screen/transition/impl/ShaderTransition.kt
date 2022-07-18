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

import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Mesh
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder
import com.badlogic.gdx.graphics.g3d.utils.RenderContext
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
import de.damios.guacamole.Preconditions
import de.damios.guacamole.gdx.graphics.QuadMeshGenerator
import de.damios.guacamole.gdx.graphics.ShaderProgramFactory
import eater.transitions.screen.transition.TimedTransition

/**
 * A transition that is using a shader to render the two transitioning screens.
 *
 *
 * The following uniforms are set before rendering and thus have to be specified
 * in the shader code:
 *
 *  * vertex shader:
 *
 *  * `uniform mat4 u_projTrans`
 *
 *  * fragment shader:
 *
 *  * `uniform sampler2D lastScreen`
 *  * `uniform sampler2D currScreen`
 *  * `uniform float progress`
 *
 *
 *
 * @version 0.4.0
 * @author damios
 *
 * @see GLTransitionsShaderTransition
 */
open class ShaderTransition
/**
 * Creates a shader transition.
 *
 *
 * The shader [has to be][.compileShader] before [.create] is called.
 *
 * @param duration
 * the duration of the transition
 * @param interpolation
 * the interpolation to use
 */
/**
 * Creates a shader transition.
 *
 *
 * The shader [has to be][.compileShader] before [.create] is called.
 *
 * @param duration
 *
 * @see .ShaderTransition
 */
@JvmOverloads constructor(
    duration: Float,
    interpolation: Interpolation? = null
) : TimedTransition(duration, interpolation) {
    /**
     * @return the used shader
     */
    var program: ShaderProgram? = null
        protected set
    protected var viewport: Viewport? = null
    private var renderContext: RenderContext? = null

    /**
     * A screen filling quad.
     */
    private var screenQuad: Mesh? = null
    private var projTransLoc = 0
    private var lastScreenLoc = 0
    private var currScreenLoc = 0
    private var progressLoc = 0

    /**
     * @param vert
     * the vertex shader code
     * @param frag
     * the fragment shader code
     * @param ignorePrepend
     * whether to ignore the code in
     * [ShaderProgram.prependFragmentCode] and
     * [ShaderProgram.prependVertexCode]
     */
    open fun compileShader(vert: String?, frag: String?, ignorePrepend: Boolean) {
        Preconditions.checkNotNull(vert, "The vertex shader cannot be null.")
        Preconditions.checkNotNull(frag, "The fragment shader cannot be null.")
        program = ShaderProgramFactory.fromString(
            vert, frag, true,
            ignorePrepend
        )
    }

    override fun create() {
        Preconditions.checkState(
            program != null,
            "The shader has to be compiled before the transition can be created!"
        )
        viewport = ScreenViewport() // Takes care of rendering the
        // transition over the whole
        // screen
        projTransLoc = program!!.getUniformLocation("u_projTrans")
        lastScreenLoc = program!!.getUniformLocation("lastScreen")
        currScreenLoc = program!!.getUniformLocation("currScreen")
        progressLoc = program!!.getUniformLocation("progress")
        renderContext = RenderContext(
            DefaultTextureBinder(DefaultTextureBinder.ROUNDROBIN)
        )
    }

    override fun render(
        delta: Float, lastScreen: TextureRegion?,
        currScreen: TextureRegion?, progress: Float
    ) {
        viewport!!.apply()
        renderContext!!.begin()
        program!!.bind()

        // Set uniforms
        program!!.setUniformMatrix(
            projTransLoc,
            viewport!!.camera.combined
        )
        program!!.setUniformf(progressLoc, progress)
        program!!.setUniformi(
            lastScreenLoc,
            renderContext!!.textureBinder.bind(lastScreen?.texture)
        )
        program!!.setUniformi(
            currScreenLoc,
            renderContext!!.textureBinder.bind(currScreen?.texture)
        )

        // Render the screens using the shader
        screenQuad!!.render(program, GL20.GL_TRIANGLE_STRIP)
        renderContext!!.end()
    }

    override fun resize(width: Int, height: Int) {
        viewport!!.update(width, height, true)
        if (screenQuad != null) screenQuad!!.dispose()
        screenQuad = QuadMeshGenerator.createFullScreenQuad(
            width, height,
            true
        )
    }

    override fun dispose() {
        if (program != null) program!!.dispose()
        if (screenQuad != null) screenQuad!!.dispose()
    }
}