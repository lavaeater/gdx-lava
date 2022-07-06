package eater.ai

import com.badlogic.ashley.core.Entity

abstract class AiAction(val name: String) {
    val considerations = mutableListOf<eater.ai.Consideration>()

    /***
     * This is open so we can simply implement a static score for something
     * like the Amble task - a task that will be performed unless some other task
     * gets a higher average score - very likely since most of my considerations would be
     * simply yes or no values
     *
     * Can be overridden to affect the value of the score with some interpolations,
     * considerations to be taken if there are more than one factor in the scoring,
     * etc etc
     * To take time into consideration, use the timepiece func in gdx-ai
     */
    open fun score(entity: Entity): Double {
        return considerations.map { it.normalizedScore(entity) }.average()
    }

    /**
     * Removes all components, settings etc related to this
     * action from the supplied entity - or puts them in a
     * paused state if the normal behavior is for the entity
     * to continue where it left off later
     */
    abstract fun abort(entity: Entity)

    /**
     * Does the thing it should do
     * Remember that actions should not hold their own state,
     * state should rather be kept on the entities themselves.
     */
    abstract fun act(entity: Entity, deltaTime: Float)
}