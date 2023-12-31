/*
 * Copyright (c) 2021 Alexander Shevelev
 *
 * Licensed under the MIT License;
 * ---------------------------------------
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.fiction.me.page_curl_lib.page_curling

import android.graphics.PointF
import kotlin.math.pow
import kotlin.math.sqrt

internal object PointsHelper {
    fun getDistance(p1: PointF, p2: PointF): Float {
        return sqrt((p1.x - p2.x.toDouble()).pow(2.0) + (p1.y - p2.y.toDouble()).pow(2.0)).toFloat()
    }

    fun getDistance(points: List<PointF>?): Float {
        require(!(points == null || points.isEmpty())) { "'points' can't be empty" }
        if (points.size == 1) return 0f
        if (points.size == 2) return getDistance(points[0], points[1])
        var sum = 0f
        val totalPoints = points.size
        for (i in 0 until totalPoints) for (j in 0 until totalPoints) if (i != j) sum += getDistance(points[i], points[j]) // Average distance
        return sum / (totalPoints * totalPoints - totalPoints)
    }
}