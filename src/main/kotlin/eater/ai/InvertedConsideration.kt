package eater.ai

import com.badlogic.gdx.math.MathUtils

class InvertedConsideration(
    name: String,
    consideration: Consideration
) : Consideration(
    name,
    { entity ->
            1f / consideration.scoreFunction(entity)
    })