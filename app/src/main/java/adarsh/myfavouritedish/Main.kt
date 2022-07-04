package adarsh.myfavouritedish

import java.util.*

fun main() {

}

class Solution {
    fun maxArea(h: Int, w: Int, horizontalCuts: IntArray, verticalCuts: IntArray): Int {
        // val array = Array(h) { IntArray(w) }
        horizontalCuts.sort()
        verticalCuts.sort()
        var maxHori:Long = horizontalCuts[0].toLong()
        var maxVer:Long = verticalCuts[0].toLong()
        for (i in 1 until horizontalCuts.size) maxHori =
            maxHori.coerceAtLeast((horizontalCuts[i] - horizontalCuts[i - 1]).toLong())
        for (i in 1 until verticalCuts.size) maxVer =
            (verticalCuts[i] - verticalCuts[i - 1]).toLong().coerceAtLeast(maxVer)
        maxHori = maxHori.coerceAtLeast((h - horizontalCuts[horizontalCuts.size - 1]).toLong())
        maxVer = (w - verticalCuts[verticalCuts.size - 1]).toLong().coerceAtLeast(maxVer)
        return ((maxHori*maxVer)%1000000007).toInt()
    }

    fun solution(ar:Array<Int>, sum: Int): Int {
        Arrays.sort(ar) { a, b -> a - b }
        return 0
    }


}