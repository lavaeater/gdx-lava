package eater.ai

import com.badlogic.gdx.math.MathUtils

class InvertedConsideration(
    name: String,
    consideration: Consideration,
    scoreRange: ClosedFloatingPointRange<Float> = 0f..1f
) : Consideration(
    name,
    scoreRange,
    { entity ->
        MathUtils.map(
            0f,
            1f,
            scoreRange.start,
            scoreRange.endInclusive,
            1f / consideration.scoreFunction(entity)
        )
    })