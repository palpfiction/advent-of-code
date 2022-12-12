package day12

import readInput
import java.lang.IllegalArgumentException

typealias Matrix<T> = MutableList<MutableList<T>>

fun emptyMatrix(x: Int, y: Int) = MutableList(x) { MutableList<Square>(y) { Start } }

fun <T> Matrix<T>.dimensions() = this.size to this[0].size

fun <T> Matrix<T>.print() {
    val (rows, columns) = this.dimensions()
    for (j in 0 until columns) {
        for (i in 0 until rows) {
            print(this[i][j])
        }
        println()
    }
}

data class Coordinate(val x: Int, val y: Int)

sealed class Square(val height: Int)

object Start : Square(1) {
    override fun toString(): String = "S"
}

object Destination : Square(26) {
    override fun toString(): String = "E"
}

class RegularSquare(height: Int) : Square(height) {
    override fun toString(): String = Char(height + 96).toString()

}

fun main() {
    fun dimensions(input: List<String>): Pair<Int, Int> = input[0].toCharArray().size to input.size

    fun parseMap(input: List<String>): Matrix<Square> {
        val (rows, columns) = dimensions(input)

        val map = emptyMatrix(rows, columns)
        input
            .forEachIndexed { y, line ->
                line.toCharArray()
                    .forEachIndexed { x, height ->
                        map[x][y] = when (height) {
                            'S' -> Start
                            'E' -> Destination
                            else -> RegularSquare(height.code - 96)
                        }

                    }
            }

        return map
    }

    fun possibleSteps(current: Coordinate, map: Matrix<Square>): List<Coordinate> {
        return listOf(
            current.copy(x = current.x + 1),
            current.copy(x = current.x - 1),
            current.copy(y = current.y + 1),
            current.copy(y = current.y - 1)
        )
            .filter {
                it.x in map.indices
                        && it.y in map[0].indices
            }
            .filter {
                map[it.x][it.y].height in 0..map[current.x][current.y].height + 1
            }
    }


    fun exploreSquare(coordinate: Coordinate, map: Matrix<Square>, paths: MutableMap<Coordinate, Int>): Coordinate {
        val possibleSteps = possibleSteps(coordinate, map)

        for (step in possibleSteps) {
            val currentDistance = paths.getOrDefault(step, Int.MAX_VALUE)
            val newDistance = paths.getOrDefault(coordinate, 0) + 1
            if (newDistance < currentDistance) {
                paths[step] = newDistance
            }
        }
        paths.remove(coordinate)
        return paths.minBy { it.value }.key
    }

    fun getStart(map: Matrix<Square>): Coordinate {
        for (i in map.indices) {
            for (j in map[0].indices) {
                if (map[i][j] is Start) return Coordinate(i, j)
            }
        }
        throw IllegalArgumentException()
    }

    fun getStartingSquares(map: Matrix<Square>): List<Coordinate> {
        val startingSquares = mutableListOf<Coordinate>()

        for (i in map.indices) {
            for (j in map[0].indices) {
                if (map[i][j].height == 1) startingSquares.add(Coordinate(i, j))
            }
        }

        return startingSquares
    }

    fun findShortest(start: Coordinate, map: Matrix<Square>): Int {
        val paths = mutableMapOf<Coordinate, Int>()

        var current = start

        while (map[current.x][current.y] !is Destination) {
            current = exploreSquare(current, map, paths)
        }

        return paths.getValue(current)
    }


    fun part1(input: List<String>): Int =
        with(parseMap(input)) {
            findShortest(getStart(this), this)
        }

    fun part2(input: List<String>): Int =
        with(parseMap(input)) {
            getStartingSquares(this)
                .minOf { start -> findShortest(start, this) }
        }

    val testInput = readInput("/day12/Day12_test")

    println(part1(testInput))
    println(part2(testInput))

    //check(part1(testInput) == 2)
    //check(part2(testInput) == 4)


    val input = readInput("/day12/Day12")
    // println(part1(input))
    println(part2(input))
}