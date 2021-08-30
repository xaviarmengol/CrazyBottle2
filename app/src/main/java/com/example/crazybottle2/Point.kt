package com.example.crazybottle2

import kotlin.math.*

data class Point (val x: Float = 0.0f, val y: Float = 0.0f) {

    fun dist (other: Point): Float {
        return sqrt((other.x-x).pow(2) + (other.y-y).pow(2))
    }

    fun module () = dist (Point(0.0f, 0.0f))

    fun angleDeg () : Float {
        var angleRad = atan2(y, x)
        if (angleRad<0) angleRad += 2* PI.toFloat()
        return (angleRad * 180.0f / PI.toFloat())
    }

    operator fun minus (other: Point ) = Point (x - other.x, y - other.y)
    operator fun times (scale: Float) = Point (x*scale, y*scale)

    infix fun dot (other: Point) : Float = (x*other.x + y*other.y)
    infix fun projectedTo (other: Point)  = (other * ((this dot other) /  other.module()))
    infix fun ortProjectedTo (other: Point) = this - (this projectedTo other)

    fun fillModuleAngle (module: Float, angleDeg: Float) : Point {
        return(Point(   module * cos(angleDeg.toDouble() * PI /180.0).toFloat(),
            module * sin(angleDeg.toDouble() * PI /180.0).toFloat()))
    }
}
