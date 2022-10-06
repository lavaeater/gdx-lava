package eater.ecs.ashley.systems

import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IntervalSystem

class EnsureEntityDef(val entityFamily: Family, val numberOfEntities: Int, val creator: ()->Unit)

class EnsureEntitySystem(vararg definition: EnsureEntityDef):IntervalSystem(1f) {
    val definitions = listOf(*definition)

    override fun updateInterval() {
        for(def in definitions) {
            val diff = def.numberOfEntities - engine.getEntitiesFor(def.entityFamily).size()
            if(diff > 0) {
                (0..diff).forEach { _ ->
                    def.creator()
                }
            }
        }

    }
}