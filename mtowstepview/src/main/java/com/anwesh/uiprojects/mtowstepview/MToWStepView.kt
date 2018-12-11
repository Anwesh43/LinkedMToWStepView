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
import android.graphics.PointF

val nodes : Int = 5
val lines : Int = 2
val scGap : Float = 0.05f
val scDiv : Double = 0.51
val color : Int = Color.parseColor("#6A1B9A")
val strokeFactor : Int = 90
val sizeFactor : Float = 2.7f
val mDeg : Float = 30f

fun Int.inverse() : Float = 1f / this

fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), Math.max(0f, this - i * n.inverse())) * n

fun Float.scaleFactor() : Float = Math.floor(this / scDiv).toFloat()

fun Float.mirrorValue(a : Int, b : Int) : Float = (1 - scaleFactor()) * a.inverse() + scaleFactor() * b.inverse()

fun Float.updateScale(dir : Float, a : Int, b : Int) : Float = dir * scGap * mirrorValue(a, b)

fun Canvas.drawMirrorLine(sc : Float, ox : Float, dx : Float, oy : Float, dy : Float, paint : Paint) {
    for (j in 0..(lines - 1)) {
        val sc : Float = sc.divideScale(j, lines)
        val sf : Float = 1f - 2 * (j % 2)
        drawLine(ox * sf, oy, (ox + (dx - ox) * sc) * sf , (oy) + (dy - oy) * sc, paint)
    }
}

fun Canvas.drawMToWNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    val gap : Float = w / (nodes + 1)
    val size : Float = gap / sizeFactor
    val sc1 : Float = scale.divideScale(0, 2)
    val sc2 : Float = scale.divideScale(1, 2)
    val sc11 : Float = sc1.divideScale(0, 2)
    val sc12 : Float = sc1.divideScale(1, 2)
    val lSize : Float = Math.sqrt(3.0).toFloat() * size
    val x : Float = -size * Math.cos((90f - mDeg) * Math.PI/180).toFloat()
    val y : Float = -size * Math.sin((90f - mDeg) * Math.PI/180).toFloat()
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    paint.strokeCap = Paint.Cap.ROUND
    paint.color = color
    save()
    translate(gap * (i + 1), h / 2)
    rotate(180f * sc2)
    drawMirrorLine(sc11,0f, x, 0f, y, paint)
    drawMirrorLine(sc12, x, x, y, y + lSize, paint)
    restore()
}

class MToWStepView(ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var dir : Float = 0f, var prevScale : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            scale += scale.updateScale(dir, lines * lines, 1)
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                cb()
            }
        }
    }

    data class Animator(var view : View, var animated : Boolean = false) {

        fun animate(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }
}