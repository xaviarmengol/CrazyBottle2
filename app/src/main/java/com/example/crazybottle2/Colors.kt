package com.example.crazybottle2

import android.graphics.Color

fun LongIntToColor(color: Long): Int {
    val a : Long = (color shr 24)
    val r : Long = (color shr 16)
    val g : Long = (color shr 8)
    val b : Long = (color shr 0)
    return Color.argb(a.toInt(), r.toInt(), g.toInt(), b.toInt())
}

fun colorsBackground (numColor: Int) : Int{

    val listColors = listOf<Int> (
        LongIntToColor(0xFFFFB300), //Vivid Yellow
        LongIntToColor(0xFF803E75), //Strong Purple
        LongIntToColor(0xFFFF6800), //Vivid Orange
        LongIntToColor(0xFFA6BDD7), //Very Light Blue
        LongIntToColor(0xFFC10020), //Vivid Red
        LongIntToColor(0xFFCEA262), //Grayish Yellow
        LongIntToColor(0xFF817066), //Medium Gray

        //The following will not be good for people with defective color vision
        LongIntToColor(0xFF007D34), //Vivid Green
        LongIntToColor(0xFFF6768E), //Strong Purplish Pink
        LongIntToColor(0xFF00538A), //Strong Blue
        LongIntToColor(0xFFFF7A5C), //Strong Yellowish Pink
        LongIntToColor(0xFF53377A), //Strong Violet
        LongIntToColor(0xFFFF8E00), //Vivid Orange Yellow
        LongIntToColor(0xFFB32851), //Strong Purplish Red
        LongIntToColor(0xFFF4C800), //Vivid Greenish Yellow
        LongIntToColor(0xFF7F180D), //Strong Reddish Brown
        LongIntToColor(0xFF93AA00), //Vivid Yellowish Green
        LongIntToColor(0xFF593315), //Deep Yellowish Brown
        LongIntToColor(0xFFF13A13), //Vivid Reddish Orange
        LongIntToColor(0xFF232C16), //Dark Olive Green
    )

    return (listColors[numColor])
}