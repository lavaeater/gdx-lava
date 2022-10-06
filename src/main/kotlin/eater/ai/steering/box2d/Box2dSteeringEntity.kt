/*******************************************************************************
 * Copyright 2014 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eater.ai.steering.box2d

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ai.steer.Steerable
import com.badlogic.gdx.ai.steer.SteeringAcceleration
import com.badlogic.gdx.ai.steer.SteeringBehavior
import com.badlogic.gdx.ai.utils.Location
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body

/** A steering entity for box2d physics engine.
 *
 * @author davebaol
 */
class Box2dSteeringEntity(
    var region: TextureRegion,
    var body: Body,
    var isIndependentFacing: Boolean,
    var _boundingRadius: Float
) : Steerable<Vector2> {
    var _tagged = false
    var _maxLinearSpeed = 0f
    var _maxLinearAcceleration = 0f
    var _maxAngularSpeed = 0f
    var _maxAngularAcceleration = 0f
    var steeringBehavior: SteeringBehavior<Vector2>? = null

    init {
        body.userData = this
    }

    override fun getPosition(): Vector2 {
        return body.position
    }

    override fun getOrientation(): Float {
        return body.angle
    }

    override fun setOrientation(orientation: Float) {
        body.setTransform(position, orientation)
    }

    override fun getLinearVelocity(): Vector2 {
        return body.linearVelocity
    }

    override fun getAngularVelocity(): Float {
        return body.angularVelocity
    }

    override fun getBoundingRadius(): Float {
        return _boundingRadius
    }

    override fun isTagged(): Boolean {
        return _tagged
    }

    override fun setTagged(tagged: Boolean) {
        this._tagged = tagged
    }

    override fun newLocation(): Location<Vector2> {
        return Box2dLocation()
    }

    override fun vectorToAngle(vector: Vector2): Float {
        return Box2dSteeringUtils.vectorToAngle(vector)
    }

    override fun angleToVector(outVector: Vector2, angle: Float): Vector2 {
        return Box2dSteeringUtils.angleToVector(outVector, angle)
    }

    fun update(deltaTime: Float) {
        if (steeringBehavior != null) {
            // Calculate steering acceleration
            steeringBehavior!!.calculateSteering(steeringOutput)

            /*
			 * Here you might want to add a motor control layer filtering steering accelerations.
			 * 
			 * For instance, a car in a driving game has physical constraints on its movement: it cannot turn while stationary; the
			 * faster it moves, the slower it can turn (without going into a skid); it can brake much more quickly than it can
			 * accelerate; and it only moves in the direction it is facing (ignoring power slides).
			 */

            // Apply steering acceleration
            applySteering(steeringOutput, deltaTime)
        }
//        wrapAround(
//            Box2dSteeringTest.pixelsToMeters(Gdx.graphics.width),
//            Box2dSteeringTest.pixelsToMeters(Gdx.graphics.height)
//        )
    }

    protected fun applySteering(steering: SteeringAcceleration<Vector2>?, deltaTime: Float) {
        var anyAccelerations = false

        // Update position and linear velocity.
        if (!steeringOutput.linear.isZero) {
            // this method internally scales the force by deltaTime
            body.applyForceToCenter(steeringOutput.linear, true)
            anyAccelerations = true
        }

        // Update orientation and angular velocity
        if (isIndependentFacing) {
            if (steeringOutput.angular != 0f) {
                // this method internally scales the torque by deltaTime
                body.applyTorque(steeringOutput.angular, true)
                anyAccelerations = true
            }
        } else {
            // If we haven't got any velocity, then we can do nothing.
            val linVel = linearVelocity
            if (!linVel.isZero(zeroLinearSpeedThreshold)) {
                val newOrientation = vectorToAngle(linVel)
                body.angularVelocity =
                    (newOrientation - angularVelocity) * deltaTime // this is superfluous if independentFacing is always true
                body.setTransform(body.position, newOrientation)
            }
        }
        if (anyAccelerations) {
            // body.activate();

            // TODO:
            // Looks like truncating speeds here after applying forces doesn't work as expected.
            // We should likely cap speeds form inside an InternalTickCallback, see
            // http://www.bulletphysics.org/mediawiki-1.5.8/index.php/Simulation_Tick_Callbacks

            // Cap the linear speed
            val velocity = body.linearVelocity
            val currentSpeedSquare = velocity.len2()
            val maxLinearSpeed = getMaxLinearSpeed()
            if (currentSpeedSquare > maxLinearSpeed * maxLinearSpeed) {
                body.linearVelocity = velocity.scl(maxLinearSpeed / Math.sqrt(currentSpeedSquare.toDouble()).toFloat())
            }

            // Cap the angular speed
            val maxAngVelocity = getMaxAngularSpeed()
            if (body.angularVelocity > maxAngVelocity) {
                body.angularVelocity = maxAngVelocity
            }
        }
    }

    // the display area is considered to wrap around from top to bottom
    // and from left to right
    protected fun wrapAround(maxX: Float, maxY: Float) {
        var k = Float.POSITIVE_INFINITY
        val pos = body.position
        if (pos.x > maxX) {
            pos.x = 0.0f
            k = pos.x
        }
        if (pos.x < 0) {
            pos.x = maxX
            k = pos.x
        }
        if (pos.y < 0) {
            pos.y = maxY
            k = pos.y
        }
        if (pos.y > maxY) {
            pos.y = 0.0f
            k = pos.y
        }
        if (k != Float.POSITIVE_INFINITY) body.setTransform(pos, body.angle)
    }

//    fun draw(batch: Batch) {
//        val pos = body.position
//        val w = region.regionWidth.toFloat()
//        val h = region.regionHeight.toFloat()
//        val ox = w / 2f
//        val oy = h / 2f
//        batch.draw(
//            region,  //
//            Box2dSteeringTest.metersToPixels(pos.x) - ox, Box2dSteeringTest.metersToPixels(pos.y) - oy,  //
//            ox, oy,  //
//            w, h,  //
//            1f, 1f,  //
//            body.angle * MathUtils.radiansToDegrees
//        ) //
//    }

    //
    // Limiter implementation
    //
    override fun getMaxLinearSpeed(): Float {
        return _maxLinearSpeed
    }

    override fun setMaxLinearSpeed(maxLinearSpeed: Float) {
        this._maxLinearSpeed = maxLinearSpeed
    }

    override fun getMaxLinearAcceleration(): Float {
        return _maxLinearAcceleration
    }

    override fun setMaxLinearAcceleration(maxLinearAcceleration: Float) {
        this._maxLinearAcceleration = maxLinearAcceleration
    }

    override fun getMaxAngularSpeed(): Float {
        return _maxAngularSpeed
    }

    override fun setMaxAngularSpeed(maxAngularSpeed: Float) {
        this._maxAngularSpeed = maxAngularSpeed
    }

    override fun getMaxAngularAcceleration(): Float {
        return _maxAngularAcceleration
    }

    override fun setMaxAngularAcceleration(maxAngularAcceleration: Float) {
        this._maxAngularAcceleration = maxAngularAcceleration
    }

    override fun getZeroLinearSpeedThreshold(): Float {
        return 0.001f
    }

    override fun setZeroLinearSpeedThreshold(value: Float) {
        throw UnsupportedOperationException()
    }

    companion object {
        private val steeringOutput = SteeringAcceleration(Vector2())
    }
}