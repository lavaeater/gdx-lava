package eater.ecs.ashley.components

import com.badlogic.gdx.math.MathUtils


abstract class PropName(val name: String)

sealed class CoolProp(val propName: PropName) {
    class FloatProperty(name: PropName, current: Float = 100f, val min: Float = 0f, val max: Float = 100f): CoolProp(name) {
        var current = current
            set(value) {
                field = MathUtils.clamp(value, min, max)
            }
        val normalizedValue: Float
                get() = MathUtils.norm(min, max, MathUtils.clamp(current, 0f, max))
    }
}

sealed class Prop(val name: String) {

    open class FloatProp(name: String, var current: Float = 100f, val min: Float = 0f, val max: Float = 100f): Prop(name) {
        val normalizedValue: Float
            get() = MathUtils.norm(min, max, MathUtils.clamp(current, 0f, max))
        class Health(current: Float = 100f,min: Float = 0f, max: Float = 100f) : FloatProp("Health", current, min, max)
        class DetectionRadius(current: Float = 100f, min: Float, max: Float): FloatProp("DetectionRadius", current, min, max)
    }
}