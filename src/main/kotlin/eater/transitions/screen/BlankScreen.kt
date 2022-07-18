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
package eater.transitions.screen

/**
 * A blank screen.
 *
 *
 * Is used internally when no screen has been
 * [pushed][ScreenManager.pushScreen] yet.
 *
 * @author damios
 */
internal class BlankScreen : ManagedScreen() {
    override fun create() {
        // do nothing
    }

    override fun render(delta: Float) {
        // do nothing
    }

    override fun hide() {
        // do nothing
    }

    override fun resize(width: Int, height: Int) {
        // do nothing
    }

    override fun dispose() {
        // do nothing
    }
}