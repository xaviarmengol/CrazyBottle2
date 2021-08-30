package com.example.crazybottle2

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.security.auth.login.LoginException

class AnimationViewModel : ViewModel() {

    var TAG = "AnimViewModel"

    var firstTime: Boolean = true
    var rotation : Float = 0.0f
    val velocity = MutableLiveData<Float>() // To directly be shown in the Layout
    var actionId : Int = 0;

    var pressed : Boolean = false
        private set

    var rotating : Boolean = false
        private set

    var origin: Point = Point()
    var final: Point = Point()

    var rotCenter: Point = Point()

    var game : Game = Game(2)

    var listOfSegments: List<String> = listOf<String>()
    var listOfActions: List<String> = listOf<String>()

    fun getColor(colorNum : Int) = colorsBackground(colorNum)


    fun getNumAngle(valueAnimation: Float, numSegments: Int) : Int {
        val segmentSize = 360f / numSegments
        var endAngle = (valueAnimation + segmentSize/2.0f) % 360.0f
        if (endAngle<0) endAngle += 360.0f

        var numAngleReturn = ((endAngle) / segmentSize).toInt()

        if (numAngleReturn <0 || numAngleReturn>=numSegments) {
            Log.e(TAG, "getNumAngle: $segmentSize $endAngle $numAngleReturn")
            numAngleReturn = 0
        }
        return numAngleReturn
    }

    fun onActionDown(pos: Point) {

        if (!rotating) {
            pressed = true
            origin = pos
            final = pos
        }
        if (firstTime) firstTime = false
        Log.i(TAG, "onActionDown: Init: $rotCenter")
    }

    fun onActionMove(pos: Point) {

        if (pressed and !rotating) {

            final = pos
            rotating = true

            val randVel = (0 until 200).random().toFloat()
            val angleMov = (final-rotCenter).angleDeg() - (origin-rotCenter).angleDeg()
            velocity.value = angleMov * 30.0f + randVel

            //velocity.value = 200.0f

            Log.i(TAG, "onActionMove: ")
        }
    }

    fun onActionUp() {
        pressed = false
        Log.i(TAG, "onActionUp: $velocity")
    }

    fun onEndAnimation(numActions : Int) {
        rotating = false

        val personNum = getNumAngle(rotation, listOfSegments.size)

        when (game.stateId.value) {
            0-> {
                game.fromPerson = personNum
                actionId = (0 until numActions).random()
            }
            1-> {
                game.toPerson = personNum
            }
        }

        game.nextState()

        Log.i(TAG, "onEndAnimation: $rotation")
    }

    class Game (private val numStates : Int = 2) {

        // Protect MutableLiveData
        private val _stateId = MutableLiveData<Int> (0)
        val stateId : LiveData<Int>
            get() {
                return _stateId
            }

        var fromPerson = 0
        var toPerson = 0

        init {
            _stateId.value = 0
        }

        fun nextState () {
            _stateId.value = _stateId.value?.plus(1)?.mod(numStates)
        }

    }

}