package eater.ecs.fleks.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import eater.ai.fleks.FleksAiAction

class FleksAi : Component<FleksAi> {
    val actions = mutableListOf<FleksAiAction>()
    private var currentAction: FleksAiAction? = null
    private var canSwitchAction = true

    fun updateAction(entity: Entity) {
        canSwitchAction = false
        actions.sortByDescending { it.updateScore(entity) }
        canSwitchAction = true
    }

    fun topAction(entity: Entity): FleksAiAction? {
        if(canSwitchAction) {
            val potentialAction = actions.first()
            if (currentAction != potentialAction) {
                currentAction?.abort(entity)
                currentAction = potentialAction
            }
        }
        return currentAction
    }




    override fun type() = FleksAi

    companion object : ComponentType<FleksAi>()
}