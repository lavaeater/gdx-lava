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
package eater.transitions.utils

import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.utils.Array

/**
 * An InputProcessor that delegates to an ordered list of other
 * [InputProcessor]s. Delegation for an event stops if a processor returns
 * `true`, which indicates that the event was handled.
 *
 *
 * This class adds some convenience methods for quickly changing all of the
 * added input processors.
 *
 * @author damios
 */
class BasicInputMultiplexer : InputMultiplexer() {
    /**
     * Removes all input processors.
     *
     * @see .clear
     */
    fun removeProcessors() {
        this.clear()
    }

    /**
     * Removes all input processors contained in the given array.
     *
     * @param processors
     * the processor to remove
     * @see .removeProcessor
     */
    fun removeProcessors(processors: Array<InputProcessor>) {
        for (p in processors) {
            removeProcessor(p)
        }
    }

    fun addProcessors(processors: Array<InputProcessor>) {
        getProcessors().addAll(processors)
    }
}