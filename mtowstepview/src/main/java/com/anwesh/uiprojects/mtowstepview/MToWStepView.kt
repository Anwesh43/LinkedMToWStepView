package com.anwesh.uiprojects.mtowstepview

/**
 * Created by anweshmishra on 11/12/18.
 */

import android.app.Activity
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Color
import android.view.View
import android.view.MotionEvent
import android.content.Context

val nodes : Int = 5
val lines : Int = 2
val scGap : Float = 0.05f
val scDiv : Double = 0.51
val color : Int = Color.parseColor("#6A1B9A")
val strokeFactor : Int = 90
val sizeFactor : Float = 2.7f

fun Int.inverse() : Float = 1f / this

fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), Math.max(0f, this - i * n.inverse())) * n

fun Float.scaleFactor() : Float = Math.floor(this / scDiv).toFloat()

fun Float.mirrorValue(a : Int, b : Int) : Float = (1 - scaleFactor()) * a.inverse() + scaleFactor() * b.inverse()

fun Float.updateScale(dir : Float, a : Int, b : Int) : Float = dir * scGap * mirrorValue(a, b)
