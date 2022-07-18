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

import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.Interpolation
import de.damios.guacamole.Preconditions
import de.damios.guacamole.gdx.graphics.ShaderCompatibilityHelper

/**
 * A transition that is using shader code conforming to the GL Transition
 * Specification v1.
 *
 *
 * This allows using the shaders provided at
 * [gl-transitions.com](https://gl-transitions.com/gallery) without
 * having to adapt their code.
 *
 *
 * The technical details: A GL Transition is a GLSL code that implements a
 * transition function which takes a `vec2 uv` pixel position and returns
 * a `vec4 color`. This color represents the mix of the `from` to
 * the `to` textures based on the variation of a contextual progress value
 * from `0.0` to `1.0`.
 *
 * @since 0.4.0
 * @author damios
 *
 * @see [Additional
 * information on the GL Transition spec](https://github.com/gl-transitions/gl-transitions.gl-transition)
 */
class GLTransitionsShaderTransition
/**
 * Creates a shader transition with code conforming to the GL Transitions
 * spec.
 *
 *
 * The shader [has to be compiled][.compileGLTransition]
 * before [.create] is called.
 *
 * @param duration
 * @param interpolation
 */
// @formatter:on
/**
 * Creates a shader transition with code conforming to the GL Transitions
 * spec.
 *
 *
 * The shader [has to be compiled][.compileGLTransition]
 * before [.create] is called.
 *
 * @param duration
 */
@JvmOverloads constructor(
    duration: Float,
    interpolation: Interpolation? = null
) : ShaderTransition(duration, interpolation) {
    /**
     * The GL Transitions shader code has to be set via this method.
     *
     *
     * Do not forget to uncomment/set the uniforms that act as transition
     * parameters! Please note that in GLSL EL (Web, Android, iOS) uniforms
     * cannot be set from within the shader code, so this has to be done in Java
     * via [.getProgram] instead!
     *
     *
     * Furthermore, do not forget to replace `ratio` in the code with your
     * screen ratio (width / height).
     *
     *
     * Ignores code in [ShaderProgram.prependFragmentCode] and
     * [ShaderProgram.prependVertexCode].
     *
     * @param glTransitionsCode
     * the GL Transitions shader code;
     */
    fun compileGLTransition(glTransitionsCode: String) {
        compileShader(
            VERT_SHADER,
            FRAG_SHADER_PREPEND + glTransitionsCode + FRAG_SHADER_POSTPEND,
            true
        )
    }

    @Deprecated("Call {@link #compileGLTransition(String)} instead!")
    override fun compileShader(vert: String?, frag: String?, ignorePrepend: Boolean) {
        Preconditions.checkNotNull(vert, "The vertex shader cannot be null.")
        Preconditions.checkNotNull(frag, "The fragment shader cannot be null.")
        program = ShaderCompatibilityHelper.fromString(vert, frag)
    }

    companion object {
        // @formatter:off
        private const val VERT_SHADER = "#ifdef GL_ES\n" +
                "precision mediump float;\n" +
                "#endif\n" +
                "\n" +
                "attribute vec3 a_position;\n" +
                "attribute vec2 a_texCoord0;\n" +
                "\n" +
                "uniform mat4 u_projTrans;\n" +
                "\n" +
                "varying vec3 v_position;\n" +
                "varying vec2 v_texCoord0;\n" +
                "\n" +
                "void main() {\n" +
                "	v_position = a_position;\n" +
                "	v_texCoord0 = a_texCoord0;\n" +
                "	gl_Position = u_projTrans * vec4(a_position, 1.0);\n" +
                "}"
        private const val FRAG_SHADER_PREPEND = "#ifdef GL_ES\n" +
                "precision mediump float;\n" +
                "#endif\n" +
                "\n" +
                "varying vec3 v_position;\n" +
                "varying vec2 v_texCoord0;\n" +
                "\n" +
                "\n" +
                "\n" +
                "uniform sampler2D lastScreen;\n" +
                "uniform sampler2D currScreen;\n" +
                "uniform float progress;\n" +
                "\n" +
                "vec4 getToColor(vec2 uv) {\n" +
                "		return texture2D(currScreen, uv);\n" +
                "}\n" +
                "\n" +
                "vec4 getFromColor(vec2 uv) {\n" +
                "		return texture2D(lastScreen, uv);\n" +
                "}\n"
        private const val FRAG_SHADER_POSTPEND = ("\nvoid main() {\n"
                + "	gl_FragColor = transition(v_texCoord0);\n" +
                "}\n")
    }
}