package eater.ecs.fleks.systems

import com.github.quillraven.fleks.IntervalSystem
import eater.core.world

class Box2dUpdateSystem(
    private val timeStep: Float,
    private val velIters: Int,
    private val posIters: Int) : IntervalSystem() {
    var accumulator = 0f
    override fun onTick() {
        val ourTime = deltaTime.coerceAtMost(timeStep * 2)
        accumulator += ourTime
        while (accumulator > timeStep) {
            world().step(timeStep, velIters, posIters)
            accumulator -= ourTime
        }
        /**
         * Perhaps add some kind of physics-add-body-queue-system to do adding of stuff
         * not inside a step
         */
    }

}